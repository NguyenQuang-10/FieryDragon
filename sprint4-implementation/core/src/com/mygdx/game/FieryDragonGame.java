package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.BeforeGameScreen;
import com.mygdx.game.screen.PauseScreen;
import com.mygdx.game.screen.StartScreen;

// Driver class for the Game
public class FieryDragonGame extends Game {
	// The menu screen to configure the game
	public BeforeGameScreen beforeGameScreen;

	// The screen that will display the actual game
	public GameScreen gameScreen;
	public PauseScreen pauseScreen;
	public StartScreen startScreen;

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
		// Create the menu screen and set it as the initial screen
		startScreen = new StartScreen(this);
		setScreen(startScreen);
	}

	// See libGDX documentation
	// Called when the application should release all resources
	@Override
	public void dispose () {
		// Dispose of resources used by the menu screen
		startScreen.dispose();
		if (beforeGameScreen != null) {
			beforeGameScreen.dispose();
		}
		// Dispose of resources used by the game screen if it exists
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		if (pauseScreen != null) {
			pauseScreen.dispose();
		}
	}
}
