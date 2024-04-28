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
    FieryDragonGame game;
    Stage stage;
    BitmapFont font;
    SpriteBatch batch;

    int inputCount = 0;

    public MenuScreen(FieryDragonGame game) {
        this.game = game;
    }


    @Override
    public void show() {
        stage = new Stage();
        font = new BitmapFont();
        batch = new SpriteBatch();

        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

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

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(game.WIDTH, game.HEIGHT, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
