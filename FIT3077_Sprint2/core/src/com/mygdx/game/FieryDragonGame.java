package com.mygdx.game;

import com.badlogic.gdx.Game;

public class FieryDragonGame extends Game {
	public MenuScreen menuScreen;
	public GameScreen gameScreen;
	public final int WIDTH = 1920;
	public final int HEIGHT = 1080;
	public int numberOfPlayers = 4;
	public boolean havePlayerAsObstacle = true;
	
	@Override
	public void create () {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	
	@Override
	public void dispose () {
		menuScreen.dispose();
		if (gameScreen != null) {
			gameScreen.dispose();
		}
	}
}
