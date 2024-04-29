package com.mygdx.game;

import com.badlogic.gdx.Game;

// Driver class for the Game
public class FieryDragonGame extends Game {
	// The menu screen to configure the game
	public MenuScreen menuScreen;

	// The screen that will display the actual game
	public GameScreen gameScreen;

	// resolution to run the game at
	public final int WIDTH = 1920;
	public final int HEIGHT = 1080;

	// number of players in the game, default is 4
	public int numberOfPlayers = 4;
	// have a player outside of their cave and on a volcano right at the start of the game,
	// to test movement mechanic
	public boolean havePlayerAsObstacle = true;

	// See libGDX documentation
	@Override
	public void create () {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	// See libGDX documentation
	@Override
	public void dispose () {
		menuScreen.dispose();
		if (gameScreen != null) {
			gameScreen.dispose();
		}
	}
}
