package com.project.game.ai.minimax;

import java.util.ArrayList;

import com.gipf.client.offline.logic.Game;
import com.project.game.ai.node.Node;
import com.project.game.ai.tree.Tree;

public class MiniMax<E> {
	protected Game game;
	protected Tree<E> tree;
	
	public MiniMax(Game game, Tree<E> tree){
		this.game = game;
		this.tree = tree;
	}
	
	
	public Node<E> miniMax(ArrayList<Node<E>> nodeList, int depth, boolean turnOfPlayer){
		int bestValue;
//		PlayerEvent bestMove;
		Node<E> bestNode;
		
		if(depth == 0 || game.getGameLogic().checkForWin())
			return null;
		
		if(turnOfPlayer == true){
			bestValue = Integer.MIN_VALUE;
//			bestMove = null;
			bestNode = null;
			for(Node<E> node: nodeList){				
				Node<E> tmpNode = miniMax(node.getChildren(), depth-1, false);
//				Node<E> tmpNode = miniMax(tree.bfSearch(node), depth-1, false);
				if(node.getValue() > bestValue){
					bestValue = node.getValue();
					bestNode = tmpNode;
				}
			}
//			return bestMove;
			return bestNode;
		}
		
		if(turnOfPlayer == false){
			bestValue = Integer.MAX_VALUE;
//			bestMove = null;
			bestNode = null;
			for(Node<E> node: nodeList){
//				PlayerEvent tmpEvent = miniMax(tree.bfSearch(node), depth-1, true);
				Node<E> tmpNode = miniMax(node.getChildren(), depth-1, true);
//				Node<E> tmpNode = miniMax(tree.bfSearch(node), depth-1, true);
				if(node.getValue() < bestValue){
					bestValue = node.getValue();
//					bestMove = node.getPlayerEvent();
					bestNode = tmpNode;
				}
			}
//			return bestMove;
			return bestNode;
		};
		return null;
	}
}
