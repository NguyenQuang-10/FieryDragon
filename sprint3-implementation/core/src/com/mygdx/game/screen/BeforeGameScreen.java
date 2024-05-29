package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.FieryDragonGame;

// Rudementary menu just for this project, won't be used in the next sprint
public class BeforeGameScreen implements Screen {
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

    boolean readyToStartGame = false;

    // Constructor
    public BeforeGameScreen(FieryDragonGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
    }


    // See libGDX documentation
    /*
        Draw a screen to configure the start of the game (e.g How many players?)
     */
    @Override
    public void show() {
        // Initialize stage, font, batch, and layout
        stage = new Stage();
        font = new BitmapFont();
        batch = new SpriteBatch();
        layout = new GlyphLayout(font, "How many players on board?\nPress 2,3, or 4");

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

        // Check for key presses to determine the number of players
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.numberOfPlayers = 2;
            readyToStartGame = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.numberOfPlayers = 3;
            readyToStartGame = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            game.numberOfPlayers = 4;
            readyToStartGame = true;
        }

        // If ready to start the game, create a new GameScreen and set it as the active screen
        if (readyToStartGame) {
            game.gameScreen = new GameScreen(game);
            game.setScreen(game.gameScreen);
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
        batch.dispose();
    }
}
