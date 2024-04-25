package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.FDBoard;
import com.mygdx.game.ChitCards.ChitCardManager;
import com.mygdx.game.ChitCards.FDChitCardManager;
import com.mygdx.game.UIComponents.FDBoardUI;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.Board.Player;
import com.mygdx.game.UIComponents.FDChitCardManagerUI;

public class GameScreen implements Screen {
    private Stage stage;

    public GameScreen() {
    }

    Player[] players = {new Player("Player 1"),
                        new Player("Player 2"),
                        new Player("Player 3"),
                        new Player("Player 4")};
    AnimalType[] volcanoMap = {
            AnimalType.BABY_DRAGON,
            AnimalType.BAT,
            AnimalType.SPIDER,
            AnimalType.SALAMANDER,
            AnimalType.BABY_DRAGON,
            AnimalType.BAT,
            AnimalType.SPIDER,
            AnimalType.SALAMANDER,
            AnimalType.BABY_DRAGON,
            AnimalType.BAT,
            AnimalType.SPIDER,
            AnimalType.SALAMANDER,
            AnimalType.BABY_DRAGON,
            AnimalType.BAT,
            AnimalType.SPIDER,
            AnimalType.SALAMANDER,
            AnimalType.BABY_DRAGON,
            AnimalType.BAT,
            AnimalType.SPIDER,
            AnimalType.SALAMANDER,
            AnimalType.BABY_DRAGON,
            AnimalType.BAT,
            AnimalType.SPIDER,
            AnimalType.SALAMANDER,
    };
    Board board = new FDBoard(players, volcanoMap);
    ChitCardManager chitCardManager = new FDChitCardManager(players);
    FDBoardUI boardUI = new FDBoardUI(200, 500, 8, 6 , board);
    FDChitCardManagerUI chitCardManagerUI = new FDChitCardManagerUI(390, 170,4,4, board, chitCardManager);

    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(boardUI);
        stage.addActor(chitCardManagerUI);
        stage.setViewport(new StretchViewport(MyGame.WIDTH, MyGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
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
