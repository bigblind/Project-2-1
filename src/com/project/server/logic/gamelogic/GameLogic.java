package com.project.server.logic.gamelogic;

import java.util.ArrayList;

import com.project.common.player.Player;
import com.project.common.player.PlayerListener;
import com.project.common.utils.Point;
import com.project.server.Server;
import com.project.server.logic.Game;
import com.project.server.logic.Row;
import com.project.server.logic.board.Board;

public abstract class GameLogic implements PlayerListener {

	protected Server server;
	protected Game game;
	protected Player currentPlayer;
	protected ArrayList<Row> removeOptions = new ArrayList<Row>();
	protected RowRemovalRequestEvent rowRemovalEvent;
	private ArrayList<PlayerChangeListener> listeners;
	private ArrayList<RowRemovalRequestListener> rrrListeners;

	public GameLogic(Game game) {
		this.game = game;
		this.listeners = new ArrayList<PlayerChangeListener>();
		this.rrrListeners = new ArrayList<RowRemovalRequestListener>();
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}

	public void removeRowFromPoints(Point start, Point end) {
		ArrayList<Row> rows = this.rowRemovalEvent.getRows();
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).getFromPoint().equals(start) && rows.get(i).getToPoint().equals(end)) {
				this.game.getBoard().removeRowAndExtensions(rows.get(i));
				rows.get(i).getPlayer().setStoneAccount(rows.get(i).getPlayer().getStoneAccount() + rows.get(i).getLength());
				this.handleExtensions(rows.get(i));
				break;
			}
		}
		this.rowRemovalEvent = null;
		this.server.sendGameUpdate();
		if (!this.handleRows()) {
			moveToNextPlayer();
		}
	}
	private boolean containsGipfStone(Point start, Point end) {
		int xx = end.getX() - start.getX();
		int yy = end.getY() - start.getY();

		int dx, dy;
		if (xx == 0) dx = 0;
		else dx = 1;
		if (yy == 0) dy = 0;
		else dy = 1;

		int length;
		if (xx == 0) length = yy;
		else if (yy == 0) length = xx;
		else length = xx;
		length++;
		for (int j = 0; j < length; j++) {
			int x = start.getX() + (j * dx);
			int y = start.getY() + (j * dy);
			if (this.game.getBoard().getGrid()[x][y] == Board.GIPF_WHITE_VALUE || this.game.getBoard().getGrid()[x][y] == Board.GIPF_BLACK_VALUE) { 
				return true;
			}
		}
		return false;
	}

	protected boolean handleRows() {
		ArrayList<Row> rows = this.game.getBoard().checkForLines();
		if (rows.size() == 1 && !containsGipfStone(rows.get(0).getFromPoint(), rows.get(0).getToPoint())) {
			Row row = rows.get(0);
			int stones = row.getLength(); // Need to look out for gipf stones in the row
			this.game.getBoard().removeRowAndExtensions(row);
			handleExtensions(row);
			row.getPlayer().setStoneAccount(row.getPlayer().getStoneAccount() + stones);

		} else if (rows.size() > 0) {
			this.server.sendGameUpdate();
			ArrayList<Row> activeRows = rowsForPlayer(this.currentPlayer.getStoneColor(), rows);
			if (activeRows.size() > 0) {
				emitRowRemovalRequest(new RowRemovalRequestEvent(activeRows));
				return true;
			} else {
				ArrayList<Row> notActiveRows;
				if (currentPlayer.getStoneColor() == Board.WHITE_VALUE) notActiveRows = rowsForPlayer(Board.BLACK_VALUE, rows);
				else notActiveRows = rowsForPlayer(Board.WHITE_VALUE, rows);

				emitRowRemovalRequest(new RowRemovalRequestEvent(notActiveRows));
				return true;
			}
		}
		return false;
	}

	// TODO this does not take into account the extension stones of a row ... this needs to happen
	// TODO gipf stones should count for more than 1 but fuck it for now
	public void removePoints(Point[] points, boolean checkRows) {
		if (!(points.length == 0)) {
			// for now this works since we only remove one color but when we fix stuff, this will break
			if (this.game.getBoard().getGrid()[points[0].getX()][points[0].getY()] == Board.WHITE_VALUE || this.game.getBoard().getGrid()[points[0].getX()][points[0].getY()] == Board.GIPF_WHITE_VALUE) {
				this.game.getPlayerOne().setStoneAccount(this.game.getPlayerOne().getStoneAccount() + points.length);
			} else {
				this.game.getPlayerTwo().setStoneAccount(this.game.getPlayerTwo().getStoneAccount() + points.length);
			}
			for (Point p : points) {
				this.game.getBoard().getGrid()[p.getX()][p.getY()] = Board.EMPTY_TILE;
			}
		}
		if (checkRows) {
			if (!this.handleRows()) {
				moveToNextPlayer();
			}
		}
	}

	protected ArrayList<Row> rowsForPlayer(int color, ArrayList<Row> possibleRows) {
		ArrayList<Row> rowsForPlayer = new ArrayList<Row>();

		for (int x = 0; x < possibleRows.size(); x++) {
			Row tmp = possibleRows.get(x);
			if (color == tmp.getPlayer().getStoneColor()) rowsForPlayer.add(new Row(tmp.getFromPoint(), tmp.getToPoint(), tmp.getPlayer(), tmp.getLength(), tmp.getWhiteExtensionStones(), tmp.getBlackExtensionStones()));
		}
		return rowsForPlayer;
	}

	protected void moveToNextPlayer() {
		if (currentPlayer == game.getPlayerOne()) {
			currentPlayer = game.getPlayerTwo();
			this.notifyListeners(new PlayerChangeEvent(game.getPlayerOne(), game.getPlayerTwo()));
		} else {
			currentPlayer = game.getPlayerOne();
			this.notifyListeners(new PlayerChangeEvent(game.getPlayerTwo(), game.getPlayerOne()));
		}
	}

	// TODO maybe this should work with the player of the row since when we are in removing state the player might be diffrent from the active one
	protected void handleExtensions(Row row) {
		if (currentPlayer.getStoneColor() == Board.WHITE_VALUE) currentPlayer.setStoneAccount(currentPlayer.getStoneAccount() + row.getWhiteExtensionStones());
		else currentPlayer.setStoneAccount(currentPlayer.getStoneAccount() + row.getBlackExtensionStones());
	}

	public Player checkPlayer(int stoneColor) {
		if (stoneColor == Board.BLACK_VALUE) return game.getPlayerTwo();
		return game.getPlayerOne();
	}

	protected Player returnWinner() {
		if (game.getPlayerOne().getStoneAccount() == 0) return game.getPlayerTwo();

		if (game.getPlayerTwo().getStoneAccount() == 0) return game.getPlayerOne();

		return null;
	}

	protected boolean checkForWin() {
		if (game.getPlayerOne().getStoneAccount() == 0 || game.getPlayerTwo().getStoneAccount() == 0) return true;

		return false;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void addPlayerChangeListener(PlayerChangeListener listener) {
		this.listeners.add(listener);
	}

	public void removePlayerChangeListener(PlayerChangeListener listener) {
		this.listeners.remove(listener);
	}

	public void notifyListeners(PlayerChangeEvent e) {
		for (PlayerChangeListener l : this.listeners)
			l.changeEventPerformed(e);
	}

	public void removeRowRemovalRequestListener(RowRemovalRequestListener l) {
		this.rrrListeners.remove(l);
	}

	public void addRowRemovalRequestListener(RowRemovalRequestListener l) {
		rrrListeners.add(l);
	}

	protected void emitRowRemovalRequest(RowRemovalRequestEvent e) {
		this.rowRemovalEvent = e;
		for (int i = 0; i < this.rrrListeners.size(); i++) {
			rrrListeners.get(i).rowRemoveRequestEventPerformed(e);
		}
	}

	public void setServer(Server server) {
		this.server = server;
	}
}