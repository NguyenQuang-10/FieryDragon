package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Board.Board;
import com.mygdx.game.ChitCards.ChitCardManager;
import com.mygdx.game.UIComponents.BoardUI;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.Board.Player;
import com.mygdx.game.UIComponents.ChitCardManagerUI;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private Stage stage;
    private FieryDragonGame game;
    Player[] players;
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
    Board board;
    ChitCardManager chitCardManager;
    BoardUI boardUI;
    ChitCardManagerUI chitCardManagerUI;

    public GameScreen(FieryDragonGame game) {
        this.game = game;
        ArrayList<Player> p = new ArrayList<>();
        for (int i = 1; i <= game.numberOfPlayers; i++) {
            p.add(new Player(String.format("Player %d", i)));
        }
        this.players = p.toArray(new Player[game.numberOfPlayers]);

        // BAD PRACTICE, THIS IS TEMPORARY ONLY, REMOVE IN NEXT SPRINT ONCE PROPER MENU IS IMPLEMENTED
        if (game.havePlayerAsObstacle) {
            players[1].isInCave = false;
        }
        board = new Board(players, volcanoMap);


        chitCardManager = new ChitCardManager(players);
        boardUI = new BoardUI(200, 500, 8, 6 , board);
        chitCardManagerUI = new ChitCardManagerUI(390, 170,4,4, board, chitCardManager);
    }

    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(boardUI);
        stage.addActor(chitCardManagerUI);
        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));
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
