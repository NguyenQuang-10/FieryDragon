package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.FieryDragonImplementations.FieryDragonBoardUI;
import com.mygdx.game.FieryDragonPrototype.AnimalType;
import com.mygdx.game.FieryDragonPrototype.AbstractPlayer;
import com.mygdx.game.FieryDragonPrototype.Player;

public class GameScreen implements Screen {
    private Stage stage;

    public GameScreen() {
    }

    AbstractPlayer[] players = {new Player("Player 1", "Players\\playerGreen.png"),
                                new Player("Player 2", "Players\\playerBlue.png"),
                                new Player("Player 3", "Players\\playerRed.png"),
                                new Player("Player 4", "Players\\playerYellow.png")
                                };
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
    FieryDragonBoardUI boardUI = new FieryDragonBoardUI(200, 500, 8, 6 , players, volcanoMap);

    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(boardUI);
        stage.setViewport(new StretchViewport(MyGame.WIDTH, MyGame.HEIGHT));
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
