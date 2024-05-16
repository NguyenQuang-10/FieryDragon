package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Board.Board;
import com.mygdx.game.ChitCards.ChitCardManager;
import com.mygdx.game.UIComponents.BoardUI;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.Board.Player;
import com.mygdx.game.UIComponents.ChitCardManagerUI;

import java.util.*;

// Screen that display the actual game
public class GameScreen implements Screen {
    static final int MAP_LENGTH = 24;

    // See libGDX documentation
    private Stage stage;
    // That Game instance, see libGDX docs
    private final FieryDragonGame game;
    // players in the game
    Player[] players;
    Board board; // Board Controller
    ChitCardManager chitCardManager; // ChitCardManager Controller
    BoardUI boardUI; // Board View
    ChitCardManagerUI chitCardManagerUI; // ChitCardManager View
    BitmapFont font;
    SpriteBatch batch;
    GlyphLayout glyphLayout;
    OrthographicCamera camera;

    // See libGDX documentation
    public GameScreen(FieryDragonGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        // Create players objects based on number of player
        ArrayList<Player> p = new ArrayList<>();
        for (int i = 1; i <= game.numberOfPlayers; i++) {
            p.add(new Player(String.format("Player %d", i)));
        }
        this.players = p.toArray(new Player[game.numberOfPlayers]);

        // randomize volcano placement
        ArrayList<AnimalType> volcanoMap = new ArrayList<>();
        while (volcanoMap.size() < MAP_LENGTH) {
            // shuffle a list of all distinct type allowed on the board, this make sure all types are used equally
            AnimalType[] allowedVolcanoTypes = {AnimalType.BAT, AnimalType.BABY_DRAGON, AnimalType.SALAMANDER, AnimalType.SPIDER};
            List<AnimalType> types = Arrays.asList(allowedVolcanoTypes);
            Collections.shuffle(types);

            volcanoMap.addAll(types);
        }

        // initialise other attributes
        board = new Board(players, volcanoMap.toArray(new AnimalType[0]));
        chitCardManager = new ChitCardManager(players);
        boardUI = new BoardUI(200, 500, 8, 6 , board, chitCardManager);
        chitCardManagerUI = new ChitCardManagerUI(390, 170,4,4, board, chitCardManager);

        font = new BitmapFont();
        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
    }

    // See libGDX documentation
    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(boardUI);
        stage.addActor(chitCardManagerUI);
        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        ScreenUtils.clear(0, 0, 0.2f, 1);
    }

    // See libGDX documentation
    @Override
    public void render(float delta) {
//        System.out.println(board.hasGameEnded());
        camera.update();
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
