package com.mygdx.game.UIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Board.*;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.ChitCards.ITurnManager;
import com.mygdx.game.Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

// Retrieves state from Board and render to the screen base on those state
// Render the board as a rectangle
// View for Board class
public class BoardUI extends FieryDragonUI {
    // map position in the board to the Coordinate on the screen
    final private Map<Integer, Coordinate> positionCoorMap = new HashMap<>();
    // map cave to their coordinate on the screen
    final private Map<Cave, Coordinate> caveCoorMap = new HashMap<>();
    // Controller/Board instance
    Board board;
    private boolean isGameStart = true;

    // height of the rectangular board (how many volcano square)
    final private int boardHeight;
    // width of the rectangular board (how many volcano square)
    final private int boardWidth;

    // number of pixel in between each volcano sprite
    final private int GUTTER_PX_SIZE = 20;

    // Map volcano type to the corresponding sprite
    final private Map<AnimalType, Texture> volcanoSprites = new HashMap<>();
    // map cave type to the corresponding sprite
    final private Map<AnimalType, Texture> caveSprites = new HashMap<>();

    // map player instance to their corresponding sprite
    final private Texture[] playerSprites = new Texture[4];
    final private ShapeRenderer shape;
    final private BitmapFont font;
    final private GlyphLayout glyphLayout;
    private Map<Player, Integer> trappedPlayer;

    // Constructor
    public BoardUI(float x, float y,
                   int boardWidth, int boardHeight,
                   Board board,
                   ITurnManager turnManager
    ) {
        super(turnManager);
        this.board = board;
        this.turnManager = turnManager;
        trappedPlayer = turnManager.getTrappedPlayer();
        AnimalType[] volcanoMap = this.board.getVolcanoMap();
        int playerCount = this.board.getPlayers().length;

        // sanity checks
        if (volcanoMap.length != boardHeight * 2 + boardWidth * 2 - 4) {
            throw new RuntimeException("[Board] Volcano map doesn't match board dimension");
        }

        if (playerCount == 0 || playerCount > 4) {
            throw new RuntimeException("[Board] Invalid number of player, please ensure there are 2-4 players");
        }

        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;

        // load sprites
        loadVolcanoSprites();
        loadCaveSprites();
        loadPlayerSprites();

        this.shape = new ShapeRenderer();
        this.glyphLayout = new GlyphLayout();
        this.font = new BitmapFont();

        // set origin to start drawing
        setX(x);
        setY(y);
    }

    // See libGDX documentation
    // Draw the board to the screen
    @Override
    public void draw(Batch batch, float parentAlpha) {
            // Call the superclass draw method after the initial message has been displayed
            super.draw(batch, parentAlpha);
            drawVolcanoes(batch);
            drawCaves(batch);
            drawPlayers(batch);
            drawText(batch);
    }

    private void drawText(Batch batch) {
        StringBuilder message = new StringBuilder();

        if (board.hasGameEnded()) {
            if (board.hasTimeLimitReached()) {
                message = new StringBuilder("You have reached the time limit,\npress 0 to pause the game please!");
                font.getData().setScale(1.5f);
                glyphLayout.setText(font, message.toString());
                // Calculate the x position for right alignment
                // Increase the right margin for further right alignment
                float xPosition = getX() + getWidth() - glyphLayout.width + 150; // Increased right margin to 50
                float yPosition = getY() + getHeight() - glyphLayout.height + 20;
                font.draw(batch, glyphLayout, xPosition, yPosition);
                font.getData().setScale(1.0f);
                return;
            } else {
                message = new StringBuilder(String.format("%s Won!!!", currentActivePlayer.getName()));
            }
        } else {
            if (isWaitingForTurnEnd()) {
                message = new StringBuilder(String.format("%s's turn has ended!!\n", currentActivePlayer.getName()));
            } else {
                for (Player p: trappedPlayer.keySet()) {
                    int trappedTurns = trappedPlayer.get(p);
                    if (trappedTurns > 0) {
                        message.append(String.format("%s's is trapped for next %d turns\n", p.getName(), trappedTurns));
                    }
                }
                message.append(String.format("\nIt is now %s's turn\n", currentActivePlayer.getName()));
            }
        }

        // Set the text for the current state
        glyphLayout.setText(font, message.toString());
        // Draw the text for the current state
        // Ensure this is only drawn if the game has not ended due to time limit
        if (!board.hasTimeLimitReached()) {
            font.draw(batch, glyphLayout, getX() - glyphLayout.width/2, getY());
        }
    }

    /*
        Load sprites for the player
     */
    private void loadPlayerSprites() {
        playerSprites[0] = new Texture(Gdx.files.internal("Players\\playerGreen.png"));
        playerSprites[1] = new Texture(Gdx.files.internal("Players\\playerRed.png"));
        playerSprites[2] = new Texture(Gdx.files.internal("Players\\playerBlue.png"));
        playerSprites[3] = new Texture(Gdx.files.internal("Players\\playerYellow.png"));
    }

    /*
        Load sprites for each player type
     */
    private void loadVolcanoSprites() {
        volcanoSprites.put(AnimalType.SALAMANDER, new Texture(Gdx.files.internal("Volcanoes\\salaVol.png")));
        volcanoSprites.put(AnimalType.BABY_DRAGON, new Texture(Gdx.files.internal("Volcanoes\\dragonVol.png")));
        volcanoSprites.put(AnimalType.SPIDER, new Texture(Gdx.files.internal("Volcanoes\\spiderVol.png")));
        volcanoSprites.put(AnimalType.BAT, new Texture(Gdx.files.internal("Volcanoes\\batVol.png")));
    }

    /*
        load sprites for each cave type
     */
    private void loadCaveSprites() {
        caveSprites.put(AnimalType.SALAMANDER, new Texture(Gdx.files.internal("Caves\\salaCave.png")));
        caveSprites.put(AnimalType.BAT, new Texture(Gdx.files.internal("Caves\\batCave.png")));
        caveSprites.put(AnimalType.BABY_DRAGON, new Texture(Gdx.files.internal("Caves\\dragonCave.png")));
        caveSprites.put(AnimalType.SPIDER, new Texture(Gdx.files.internal("Caves\\spiderCave.png")));
    }

    private Coordinate getPlayerScreenCoordinate(Player player) {
        if (player.isInCave) {
            Cave playerCave = board.getPlayerCave(player);
            return caveCoorMap.get(playerCave);
        } else {
            return positionCoorMap.get(board.getPlayerPosition(player));
        }
    }

    /*
        Draw players in the board on to the screen
        @param batch - Batch instance using to draw, see libGDX docs
     */
    private void drawPlayers(Batch batch) {

        Player[] players = board.getPlayers();
        for (int i = 0; i < players.length; i++) {
            Coordinate drawCoor = getPlayerScreenCoordinate(players[i]);

            Texture sprite = playerSprites[i];
            Color color = getColor();
            batch.setColor(color.r, color.b, color.g, color.a * 0.5f);
            batch.draw(sprite, drawCoor.x, drawCoor.y);
            batch.setColor(color.r, color.b, color.g, color.a);
        }

        batch.end();

        Coordinate hlCoor = getPlayerScreenCoordinate(currentActivePlayer);

        shape.setProjectionMatrix(batch.getProjectionMatrix());
        shape.setTransformMatrix(batch.getTransformMatrix());
        shape.translate(0, 0, 0);

        Texture sprite = playerSprites[0];
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.CYAN);

        float lineWidth = 10;
        float spWidth = sprite.getWidth();
        float spHeight = sprite.getHeight();
        shape.rectLine(hlCoor.x, hlCoor.y, hlCoor.x, hlCoor.y + spHeight, lineWidth);
        shape.rectLine(hlCoor.x + spWidth, hlCoor.y, hlCoor.x + spWidth, hlCoor.y + spHeight, lineWidth);
        shape.rectLine(hlCoor.x, hlCoor.y, hlCoor.x + spWidth, hlCoor.y, lineWidth);
        shape.rectLine(hlCoor.x, hlCoor.y + spHeight, hlCoor.x + spWidth, hlCoor.y + spHeight, lineWidth);
        shape.end();

        batch.begin();
    }

    /*
        draw volcanoes on to the screen
        @param batch - Batch instance using to draw, see libGDX docs
     */
    private void drawVolcanoes(Batch batch) {
        AnimalType[] volcanoMap = board.getVolcanoMap();

        float yOffset = getX();
        float xOffset = getY();

        int bottomLim = this.boardWidth - 1; // volcanoMap[0..bottomLim] are volcanoes on the bottom row
        int rightLim = bottomLim + (this.boardHeight - 2); // volcanoMap[bottomLim..rightLim] are volcanoes on the right
        int topLim = rightLim + this.boardWidth; // same logic
        // leftLim is just whatever that haven't been iterated across

        for (int i = 0; i < volcanoMap.length; i++ ){
            AnimalType volcanoType = volcanoMap[i];
            Texture volcanoSprite = volcanoSprites.get(volcanoType);
            batch.draw(volcanoSprite, xOffset, yOffset);

            positionCoorMap.put(i, new Coordinate(xOffset, yOffset));

            if (i <= bottomLim) { // drawing the bottom side
                // if this is the last sprite in the bottom side, reposition offset for right side
                if (i == bottomLim) {
                    yOffset += volcanoSprite.getHeight() + GUTTER_PX_SIZE;
                } else {
                    xOffset += volcanoSprite.getWidth() + GUTTER_PX_SIZE;
                }

            } else if (i <= rightLim ) { // drawing the right side
                yOffset += volcanoSprite.getHeight() + GUTTER_PX_SIZE;
            } else if (i <= topLim) {
                if (i == topLim) {
                    yOffset -= volcanoSprite.getHeight() + GUTTER_PX_SIZE;
                } else {
                    xOffset -= volcanoSprite.getWidth() + GUTTER_PX_SIZE;
                }
            } else {
                yOffset -= volcanoSprite.getHeight() + GUTTER_PX_SIZE;
            }
        }
    }

    /*
        draw caves on to the screen
        @param batch - Batch instance using to draw, see libGDX docs
     */
    private void drawCaves(Batch batch) {
        Cave[] caves = board.getCaves();

        int bottomLim = this.boardWidth - 1; // volcanoMap[0..bottomLim] are volcanoes on the bottom row
        int rightLim = bottomLim + (this.boardHeight - 2); // volcanoMap[bottomLim..rightLim] are volcanoes on the right
        int topLim = rightLim + this.boardWidth; // same logic
        // leftLim is just whatever that haven't been iterated across


        for (Cave currCave : caves) {
            Coordinate coor = positionCoorMap.get(currCave.position);
            Texture sprite = caveSprites.get(currCave.type);

            float x = coor.x;
            float y = coor.y;

            if (currCave.position <= bottomLim) {
                y -= sprite.getHeight() + GUTTER_PX_SIZE;
            } else if (currCave.position <= rightLim) {
                x += sprite.getWidth() + GUTTER_PX_SIZE;
            } else if (currCave.position <= topLim) {
                y += sprite.getHeight() + GUTTER_PX_SIZE;
            } else {
                x -= sprite.getWidth() + GUTTER_PX_SIZE;
            }

            caveCoorMap.put(currCave, new Coordinate(x, y));
            batch.draw(sprite, x, y);
        }

    }

    @Override
    public void startTurnChange() {
        super.startTurnChange();
    }

    @Override
    public void endTurnChange() {
        trappedPlayer = turnManager.getTrappedPlayer();
        super.endTurnChange();
    }
}
