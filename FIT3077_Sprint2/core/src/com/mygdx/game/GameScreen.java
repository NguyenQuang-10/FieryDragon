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

// Screen that display the actual game
public class GameScreen implements Screen {
    // See libGDX documentation
    private Stage stage;
    // That Game instance, see libGDX docs
    private FieryDragonGame game;
    // players in the game
    Player[] players;

    // topology of the Board
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
    Board board; // Board Controller
    ChitCardManager chitCardManager; // ChitCardManager Controller
    BoardUI boardUI; // Board View
    ChitCardManagerUI chitCardManagerUI; // ChitCardManager View

    // See libGDX documentation
    public GameScreen(FieryDragonGame game) {
        this.game = game;

        // Create players objects based on number of player
        ArrayList<Player> p = new ArrayList<>();
        for (int i = 1; i <= game.numberOfPlayers; i++) {
            p.add(new Player(String.format("Player %d", i)));
        }
        this.players = p.toArray(new Player[game.numberOfPlayers]);

        // Move a player outside of their cave right from the start
        // BAD PRACTICE, THIS IS TEMPORARY ONLY, REMOVE IN NEXT SPRINT ONCE PROPER MENU IS IMPLEMENTED
        if (game.havePlayerAsObstacle) {
            players[1].isInCave = false;
        }

        // initialise other attributes
        board = new Board(players, volcanoMap);
        chitCardManager = new ChitCardManager(players);
        boardUI = new BoardUI(200, 500, 8, 6 , board, chitCardManager);
        chitCardManagerUI = new ChitCardManagerUI(390, 170,4,4, board, chitCardManager);
    }

    // See libGDX documentation
    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(boardUI);
        stage.addActor(chitCardManagerUI);
        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

    // See libGDX documentation
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.act(delta);
        stage.draw();
    }

    // See libGDX documentation
    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
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
