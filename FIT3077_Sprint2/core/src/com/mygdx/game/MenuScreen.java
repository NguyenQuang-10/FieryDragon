package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

// Rudementary menu just for this project, won't be used in the next sprint
public class MenuScreen implements Screen {
    // Game instance - See libGDX documentation
    FieryDragonGame game;

    // See libGDX documentation
    Stage stage;

    // See libGDX documentation
    BitmapFont font;

    // See libGDX documentation
    SpriteBatch batch;

    // how many time user has input
    // use to determine what game attribute to configure
    int inputCount = 0;

    // Constructor
    public MenuScreen(FieryDragonGame game) {
        this.game = game;
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

        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

    // See libGDX documentation
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        int x = 10;
        int y = 125;
        if (inputCount == 0) {
            batch.begin();
            font.draw(batch, "How many player on board?\n Press 2,3 or 4\n\nYou are always the Green player ",
                    x, y);
            batch.end();
        } else if (inputCount == 1) {
            batch.begin();
            font.draw(batch, "Initialise board with a player standing in volcano?\n (As obstacle to test movement)\n Press Y for Yes, N for No",
                    x, y);
            batch.end();
        }


        if (inputCount == 0){
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
                game.numberOfPlayers = 2;
                inputCount += 1;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                game.numberOfPlayers = 3;
                inputCount += 1;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                game.numberOfPlayers = 4;
                inputCount += 1;
            }
        } else if (inputCount == 1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                game.havePlayerAsObstacle = true;
                inputCount += 1;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                game.havePlayerAsObstacle = false;
                inputCount += 1;
            }
        } else if (inputCount > 1) {
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
