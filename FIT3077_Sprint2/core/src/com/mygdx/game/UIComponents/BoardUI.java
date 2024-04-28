package com.mygdx.game.UIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Board.*;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

// uses the bridge design pattern with Fiery Dragon board, seperates UI and logic
// IDK if dependency inversion is a pattern but it is used here as well
public class BoardUI extends Actor {
    final private Map<Integer, Coordinate> positionCoorMap = new HashMap<>();
    final private Map<Cave, Coordinate> caveCoorMap = new HashMap<>();
    Board board;
    final private int boardHeight;
    final private int boardWidth;
    final private int GUTTER_PX_SIZE = 20;
    final private Map<AnimalType, Texture> volcanoSprites = new HashMap<>();
    final private Map<AnimalType, Texture> caveSprites = new HashMap<>();
    final private Texture[] playerSprites = new Texture[4];


    public BoardUI(float x, float y,
                   int boardWidth, int boardHeight,
                   Board board) {
        this.board = board;
        AnimalType[] volcanoMap = this.board.getVolcanoMap();
        int playerCount = this.board.getPlayers().length;

        if (volcanoMap.length != boardHeight * 2 + boardWidth * 2 - 4) {
            throw new RuntimeException("[Board] Volcano map doesn't match board dimension");
        }

        if (playerCount == 0 || playerCount > 4) {
            throw new RuntimeException("[Board] Invalid number of player, please ensure there are 2-4 players");
        }

        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;

        loadVolcanoSprites();
        loadCaveSprites();
        loadPlayerSprites();

        setX(x);
        setY(y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawVolcanoes(batch);
        drawCaves(batch);
        drawPlayers(batch);
    }

    private void loadPlayerSprites() {
        playerSprites[0] = new Texture(Gdx.files.internal("Players\\playerGreen.png"));
        playerSprites[1] = new Texture(Gdx.files.internal("Players\\playerRed.png"));
        playerSprites[2] = new Texture(Gdx.files.internal("Players\\playerBlue.png"));
        playerSprites[3] = new Texture(Gdx.files.internal("Players\\playerYellow.png"));
    }
    private void loadVolcanoSprites() {
        volcanoSprites.put(AnimalType.SALAMANDER, new Texture(Gdx.files.internal("Volcanoes\\salaVol.png")));
        volcanoSprites.put(AnimalType.BABY_DRAGON, new Texture(Gdx.files.internal("Volcanoes\\dragonVol.png")));
        volcanoSprites.put(AnimalType.SPIDER, new Texture(Gdx.files.internal("Volcanoes\\spiderVol.png")));
        volcanoSprites.put(AnimalType.BAT, new Texture(Gdx.files.internal("Volcanoes\\batVol.png")));
    }

    private void loadCaveSprites() {
        caveSprites.put(AnimalType.SALAMANDER, new Texture(Gdx.files.internal("Caves\\salaCave.png")));
        caveSprites.put(AnimalType.BAT, new Texture(Gdx.files.internal("Caves\\batCave.png")));
        caveSprites.put(AnimalType.BABY_DRAGON, new Texture(Gdx.files.internal("Caves\\dragonCave.png")));
        caveSprites.put(AnimalType.SPIDER, new Texture(Gdx.files.internal("Caves\\spiderCave.png")));
    }

    private void drawPlayers(Batch batch) {
        float PLAYER_ALPHA = 0.5f;
        Player[] players = board.getPlayers();
        for (int i = 0; i < players.length; i++) {
            float x;
            float y;

            if (players[i].isInCave) {
                Cave playerCave = board.getPlayerCave(players[i]);
                Coordinate caveCoor = caveCoorMap.get(playerCave);

                x = caveCoor.x;
                y = caveCoor.y;
            } else {
                Coordinate positionCoor = positionCoorMap.get(players[i].getPosition());
                x = positionCoor.x;
                y = positionCoor.y;
            }

            Texture sprite = playerSprites[i];
            Color color = getColor();
            batch.setColor(color.r, color.b, color.g, color.a * PLAYER_ALPHA);
            batch.draw(sprite, x, y);
            batch.setColor(color.r, color.b, color.g, color.a);
        }
    }

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

    private void drawCaves(Batch batch) {
        Cave[] caves = board.getCaves();

        int bottomLim = this.boardWidth - 1; // volcanoMap[0..bottomLim] are volcanoes on the bottom row
        int rightLim = bottomLim + (this.boardHeight - 2); // volcanoMap[bottomLim..rightLim] are volcanoes on the right
        int topLim = rightLim + this.boardWidth; // same logic
        // leftLim is just whatever that haven't been iterated across


        for (int i = 0; i < caves.length; i++) {
            Cave currCave = caves[i];
            Coordinate coor = positionCoorMap.get(currCave.position);
            Texture sprite = caveSprites.get(currCave.type);

            float x = coor.x;
            float y = coor.y;

            if (currCave.position <= bottomLim) {
                y -= sprite.getHeight() + GUTTER_PX_SIZE;
            } else if (currCave.position <= rightLim ) {
                x += sprite.getWidth() + GUTTER_PX_SIZE;
            } else if (currCave.position <= topLim) {
                y += sprite.getHeight() + GUTTER_PX_SIZE;
            } else {
                x -= sprite.getWidth() + GUTTER_PX_SIZE;
            }

            caveCoorMap.put(currCave, new Coordinate(x,y));
            batch.draw(sprite, x,y);
        }

    }
}
