package com.gipf.client.player.bot.algorithm;

import com.gipf.client.game.player.Player;
import com.gipf.client.offline.logic.Game;
import com.gipf.client.player.bot.evaluation.EvaluationFunction;
import com.gipf.client.player.bot.evaluation.Evaluator;
import com.gipf.client.player.bot.generator.GameState;
import com.gipf.client.player.bot.generator.StateGenerator;
import com.gipf.client.utils.Point;

public abstract class Algorithm {

	protected StateGenerator generator;
	protected Evaluator evaluator;
	protected Game game;
	
	public Algorithm(EvaluationFunction function) {
		this.generator = new StateGenerator();
		this.evaluator = new Evaluator(function);
	}
	
	public void setEvaluationFunction(EvaluationFunction function) {
		this.evaluator.setEvaulationFunction(function);
	}
	
	public abstract Point[] returnBestMove(GameState gameState, Player player);
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return this.game;
	}
	
	public int evaluate(GameState state){
		return this.evaluator.evaluate(state.getGame().getBoard(),
									   state.getGame().getPlayerOne().getStoneAccount(),
									   state.getGame().getPlayerTwo().getStoneAccount());
	}
}
