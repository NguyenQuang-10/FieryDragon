package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

// TODO: we got up until Making the Bucket Move

public class MyGame extends Game {
	private GameScreen gameScreen;
	public final static int WIDTH = 1920;
	public final static int HEIGHT = 1080;
	public Assets assets = FieryDragonAssets.getInstance();
	
	@Override
	public void create () {
		assets.preload();
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	
	@Override
	public void dispose () {
		gameScreen.dispose();
	}
}
