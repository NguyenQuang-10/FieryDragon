package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.FieryDragonGame;

// Rudementary menu just for this project, won't be used in the next sprint
public class MenuScreen implements Screen {
    FieryDragonGame game;
    Stage stage;
    BitmapFont font;
    SpriteBatch batch;
    OrthographicCamera camera;

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
            font.draw(batch, "Initialise board with 2 Players [default is 4]\n Press Y for Yes, N for No\n\nYou are the player on the right if there is 2 players\nYou are the bottom player if there is 4 players",
                    x ,  y);
            batch.end();
        } else if (inputCount == 1) {
            batch.begin();
            font.draw(batch, "Initialise board with a player standing in volcano?\n (As obstacle to test movement)\n Press Y for Yes, N for No",
                    x,  y);
            batch.end();
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            inputCount += 1;

            switch (inputCount) {
                case 1:
                    game.numberOfPlayers = 2;
                    break;
                case 2:
                    game.havePlayerAsObstacle = true;
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            inputCount += 1;

            switch (inputCount) {
                case 1:
                    game.numberOfPlayers = 4;
                    break;
                case 2:
                    game.havePlayerAsObstacle = false;
                    break;
            }
        }

        if (inputCount == 2) {
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
