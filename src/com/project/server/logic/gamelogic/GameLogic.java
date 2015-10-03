package com.project.server.logic.gamelogic;

import java.util.ArrayList;

import com.project.common.player.Player;
import com.project.common.player.PlayerListener;
import com.project.server.logic.Game;
import com.project.server.logic.Row;
import com.project.server.logic.board.Board;

public abstract class GameLogic implements PlayerListener {

	protected Game game;
	protected Player currentPlayer;
	protected ArrayList<Row> removeOptions = new ArrayList<Row>();
	private ArrayList<PlayerChangeListener> listeners;

	public GameLogic(Game game) {
		this.game = game;
		this.listeners = new ArrayList<PlayerChangeListener>();
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
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

	public Player checkPlayer(int stoneColor) {
		if (stoneColor == Board.BLACK_VALUE)
			return game.getPlayerTwo();
		return game.getPlayerOne();
	}

	protected Player returnWinner() {
		if (game.getPlayerOne().getStoneAccount() == 0)
			return game.getPlayerTwo();

		if (game.getPlayerTwo().getStoneAccount() == 0)
			return game.getPlayerOne();

		return null;
	}

	protected boolean checkForWin() {
		if (game.getPlayerOne().getStoneAccount() == 0 || game.getPlayerTwo().getStoneAccount() == 0)
			return true;

		return false;
	}

	public Player getActivePlayer() {
		return currentPlayer;
	}

	public void addPlayerChangeListener(PlayerChangeListener listener) {
		this.listeners.add(listener);
	}

	public void removePlayerChangeListener(PlayerChangeListener listener) {
		this.listeners.remove(listener);
	}

	private void notifyListeners(PlayerChangeEvent e) {
		for (PlayerChangeListener l : this.listeners)
			l.changeEventPerformed(e);
	}
}