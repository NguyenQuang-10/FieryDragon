package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.io.Console;

// Rudementary menu just for this project, won't be used in the next sprint
public class MenuScreen implements Screen {
    // Game instance - See libGDX documentation
    FieryDragonGame game;

    // See libGDX documentation
    Stage stage;

    // See libGDX documentation
    BitmapFont font;
    GlyphLayout layout;

    // See libGDX documentation
    SpriteBatch batch;
    OrthographicCamera camera;

    boolean readyToStartGame = false;

    // Constructor
    public MenuScreen(FieryDragonGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
    }


    // See libGDX documentation
    /*
        Draw a screen to configure the start of the game (e.g How many players?)
     */
    @Override
    public void show() {
        stage = new Stage();
        font = new BitmapFont();
        batch = new SpriteBatch();
        layout  = new GlyphLayout(font, "How many player on board?\nPress 2,3 or 4");

        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

    // See libGDX documentation
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        batch.begin();

        float drawX = game.WIDTH / 2f - layout.width / 2f;
        float drawY = game.HEIGHT / 2f + layout.height / 2f;
        font.draw(batch, layout, drawX, drawY);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            game.numberOfPlayers = 2;
            readyToStartGame = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.numberOfPlayers = 3;
            readyToStartGame = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            game.numberOfPlayers = 4;
            readyToStartGame = true;
        }

        if (readyToStartGame) {
            game.gameScreen = new GameScreen(game);
            game.setScreen(game.gameScreen);
        }

    }

    // See libGDX documentation
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(game.WIDTH, game.HEIGHT, true);
    }

    // See libGDX documentation
    @Override
    public void pause() {

    }

    // See libGDX documentation
    @Override
    public void resume() {

    }

    // See libGDX documentation
    @Override
    public void hide() {

    }

    // See libGDX documentation
    @Override
    public void dispose() {

    }
}
