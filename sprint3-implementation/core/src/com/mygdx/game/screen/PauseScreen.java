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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.FieryDragonGame;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class PauseScreen implements Screen {
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
    GameScreen saveGameScreen;

    // Constructor
    public PauseScreen(FieryDragonGame game, GameScreen gameScreen) {
        this.game = game;
        this.saveGameScreen = gameScreen;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
    }


    // See libGDX documentation
    @Override
    public void show() {
        // Initialize stage, font, batch, and layout
        stage = new Stage();
        font = new BitmapFont();
        batch = new SpriteBatch();
        layout = new GlyphLayout(font, " Option\n Press 1 to continue\n Press 2 to restart \n Press 3 to quit game and not save \n Press 4 to quit and save");
        // Set the viewport for the stage
        stage.setViewport(new StretchViewport(game.WIDTH, game.HEIGHT));

        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    // Render method called every frame
    @Override
    public void render(float delta) {
        // Clear the screen with a dark blue color
        ScreenUtils.clear(0, 0, 0.2f, 1);
        batch.begin();

        // Calculate the position to draw the text centered on the screen
        float drawX = game.WIDTH / 2f - layout.width / 2f;
        float drawY = game.HEIGHT / 2f + layout.height / 2f;

        // Draw the text on the screen
        font.draw(batch, layout, drawX, drawY);
        batch.end();

        Yaml yaml = new Yaml();
        Map<String, Object> yamlData;
        Path path = Paths.get("save_file.yaml");

        // Check for key presses to determine action
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            this.resume();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.dispose();
            game.beforeGameScreen = new BeforeGameScreen(game);
            game.setScreen(game.beforeGameScreen);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            // quit and not save

            FileHandle file = Gdx.files.local("save_file.yaml");
            String yamlString = file.readString();

            yamlData = yaml.load(yamlString);
            yamlData.put("saved", false);

            yamlString = yaml.dump(yamlData);

            file.writeString(yamlString, false);

            Gdx.app.exit();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            // quit and save
            saveGameScreen.save();
            Gdx.app.exit();
        }
    }


    // See libGDX documentation
    @Override
    public void resize(int width, int height) {
        // Update the viewport of the stage when the window is resized
        stage.getViewport().update(game.WIDTH, game.HEIGHT, true);
    }

    // Called when the game is paused
    @Override
    public void pause() {

    }

    // Called when the game is resumed from a paused state
    @Override
    public void resume() {
        game.setScreen(this.saveGameScreen);
    }

    // Called when this screen is no longer the current screen
    @Override
    public void hide() {
    }

    // Called when this screen should release all resources
    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
