package com.project.logic.gamelogic;

import java.util.ArrayList;

import com.project.logic.Game;
import com.project.logic.Row;
import com.project.logic.board.Board;
import com.project.logic.player.Player;
import com.project.logic.player.PlayerEvent;
import com.project.logic.player.PlayerListener;

public abstract class GameLogic implements PlayerListener {

	protected Game game;
	protected Player currentPlayer;
	protected ArrayList<Row> removeOptions = new ArrayList<Row>(); 
	
	public GameLogic(Game game) {
		this.game = game;
		this.currentPlayer = game.getPlayerOne();
	}

	public abstract void loop();

	public void eventPerformed(PlayerEvent e) {
		
	}
	
	protected void moveToNextPlayer(){
		if(currentPlayer == game.getPlayerOne()){
			currentPlayer = game.getPlayerTwo();
		}else{
			currentPlayer = game.getPlayerOne();
		}
	}
	
	
	public Player checkPlayer(int stoneColor){
		if(stoneColor == Board.BLACK_VALUE) return game.getPlayerTwo();
		return game.getPlayerOne();
	}
	
	
	private Player returnWinner(){
		if(game.getPlayerOne().getStoneAccount() == 0)
			return game.getPlayerTwo();
		
		if(game.getPlayerTwo().getStoneAccount() == 0)
			return game.getPlayerOne();
		
		return null;
	}
	
	
	protected boolean checkForWin(){
		if(game.getPlayerOne().getStoneAccount() == 0 || game.getPlayerTwo().getStoneAccount() == 0)
			return true;
		
		return false;
	}

	public Player getActivePlayer() {
		return currentPlayer;
	}
	
	
}