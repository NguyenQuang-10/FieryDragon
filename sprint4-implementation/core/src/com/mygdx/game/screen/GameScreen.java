package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Board.Board;
import com.mygdx.game.ChitCards.ChitCardManager;
import com.mygdx.game.FieryDragonGame;
import com.mygdx.game.UIComponents.BoardUI;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.Board.Player;
import com.mygdx.game.UIComponents.ChitCardManagerUI;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    GlyphLayout pauseText;
    float timeLeft = 300;

    // See libGDX documentation
    // Constructor for GameScreen class, initializes the game objects and UI elements
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

        // generate AnimalType array
        Map<String, AnimalType> animalTypeMap = new HashMap<>();
        for (AnimalType animalType : AnimalType.values()) {
            animalTypeMap.put(animalType.name(), animalType);
        }

        // initialise other attributes
        Yaml yaml = new Yaml();
        Map<String, List<String>> yamlData = new HashMap<>();
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("save_file.yaml"));
            yamlData = yaml.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String load_option = String.valueOf(yamlData.get("load_option"));
        board = new Board(players, load_option, animalTypeMap);


        chitCardManager = new ChitCardManager(players, load_option, animalTypeMap);
        boardUI = new BoardUI(170, 690, board, chitCardManager);
        chitCardManagerUI = new ChitCardManagerUI(350, 160, board, chitCardManager);

        font = new BitmapFont();
        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
    }

    public void save() {
        HashMap<String, Object> data = new HashMap<>();

        Map<String, List<String>> boardData = board.save();
        data.put("boardCustom", boardData.get("boardCustom"));
        data.put("playerPositionCustom", boardData.get("playerPositionCustom"));
        data.put("cavePosition", boardData.get("cavePosition"));
        data.put("volcanoCardSize", boardData.get("volcanoCardSize"));
        data.put("playerDistanceFromCaveCustom", boardData.get("playerDistanceFromCaveCustom"));
        Map<String, List<String>> chitCardData = chitCardManager.saveChitCard();
        data.put("chitCardType", chitCardData.get("chitCardType"));
        data.put("chitCardNumber", chitCardData.get("chitCardNumber"));
        data.put("chitCardFlipped", chitCardData.get("chitCardFlipped"));

        Map<String, Integer> playerData = chitCardManager.saveCurrentPlayer();
        data.put("currentPlayer", playerData.get("currentPlayer"));
        data.put("playerNumber", String.valueOf(board.getPlayers().length));

        data.put("saved", true);

        FileHandle file = Gdx.files.local("save_file.yaml");
        Yaml yaml = new Yaml();

        String yamlString = yaml.dump(data);

        file.writeString(yamlString, false);
    }

    // See libGDX documentation
    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(boardUI);
        stage.addActor(chitCardManagerUI);
        stage.setViewport(new FitViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        ScreenUtils.clear(0, 0, 0.2f, 1);
    }

    // See libGDX documentation
    // Render method called every frame
    @Override
    public void render(float delta) {
//        System.out.println(board.hasGameEnded());
        camera.update();
        // Clear the screen with a dark blue color
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.act(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            this.pause();
        }
        stage.draw();
        batch.begin();

        if (timeLeft <= 0){
            timeLeft = 0;
        } else {
            timeLeft -= delta;
        }
        int minuteLeft = (int) Math.floor(timeLeft / 60);
        int secondLeft = (int) Math.floor(timeLeft % 60);
        pauseText = new GlyphLayout(font, String.format("Press 0 to pause the game\nTime left: %d min %d sec", minuteLeft, secondLeft));

        // Calculate the position to draw the text centered o
        float drawX = 150;
        float drawY = stage.getViewport().getWorldHeight() / 2f + pauseText.height / 2f;
        // Draw the text on the screen
        font.draw(batch, pauseText, drawX, drawY);
        batch.end();

    }

    // See libGDX documentation
    // Called when the window is resized
    @Override
    public void resize(int width, int height) {
        // Update the viewport of the stage
        stage.getViewport().update(width, height, true);
    }

    // See libGDX documentation
    // Called when the game is paused
    @Override
    public void pause() {
        game.pauseScreen = new PauseScreen(game,this);
        game.setScreen(game.pauseScreen);
    }

    // See libGDX documentation
    // Called when the game is resumed from a paused state
    @Override
    public void resume() {

    }

    // See libGDX documentation
    // Called when this screen is no longer the current screen
    @Override
    public void hide() {

    }

    // See libGDX documentation
    // Called when this screen should release all resources
    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }
}
