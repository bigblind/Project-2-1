package com.project.server;

import com.project.common.player.Player;

public abstract class Server {

	public abstract void sendClientInit();
	
	public abstract void sendGameUpdate();
	
	public abstract void sendMoveValidity(boolean valid);

	public abstract void receive(byte[] bytes);
	
	public abstract void sendWinLoseUpdate(Player player);
	
	public abstract void showPresentationBoard(int index); //TODO this needs to be removed, this is for presentation purposes
}
