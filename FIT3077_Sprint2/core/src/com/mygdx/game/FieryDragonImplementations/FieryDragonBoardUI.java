package com.mygdx.game.FieryDragonImplementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Assets;
import com.mygdx.game.FieryDragonAssets;
import com.mygdx.game.FieryDragonPrototype.*;
import com.mygdx.game.Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

// uses the bridge design pattern with Fiery Dragon board, seperates UI and logic
// IDK if dependency inversion is a pattern but it is used here as well
public class FieryDragonBoardUI extends Actor {
    final private Map<Integer, Coordinate> positionCoorMap = new HashMap<>();
    final private Map<Cave, Coordinate> caveCoorMap = new HashMap<>();
    Board board;
    final private int boardHeight;
    final private int boardWidth;
    final private int GUTTER_PX_SIZE = 20;


    public FieryDragonBoardUI(float x, float y,
                              int boardWidth, int boardHeight,
                              AbstractPlayer[] players,
                              AnimalType[] volcanoMap) {
        this.board = new FieryDragonBoard();
        this.board.init(players, volcanoMap);

        if (volcanoMap.length != boardHeight * 2 + boardWidth * 2 - 4) {
            throw new RuntimeException("[Board] Volcano map doesn't match board dimension");
        }
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setX(x);
        setY(y);
    }

    private void drawPlayers(Batch batch) {
        float PLAYER_ALPHA = 0.5f;
        AbstractPlayer[] players = board.getPlayers();
        for (int i = 0; i < players.length; i++) {
            float x;
            float y;

            if (players[i].isInCave()) {
                Cave playerCave = board.getPlayerCave(players[i]);
                Coordinate caveCoor = caveCoorMap.get(playerCave);

                x = caveCoor.x;
                y = caveCoor.y;
            } else {
                Coordinate positionCoor = positionCoorMap.get(players[i].getPosition());
                x = positionCoor.x;
                y = positionCoor.y;
            }

            Texture sprite = FieryDragonAssets.getInstance().getSprite(players[i].getName());
            Color color = getColor();
            batch.setColor(color.r, color.b, color.g, color.a * PLAYER_ALPHA);
            batch.draw(sprite, x, y);
            batch.setColor(color.r, color.b, color.g, color.a);
        }
    }

    private void drawVolcanoes(Batch batch) {
        AnimalType[] volcanoMap = board.getVolcanoMap();
        Assets assets = FieryDragonAssets.getInstance();


        float yOffset = getX();
        float xOffset = getY();

        int bottomLim = this.boardWidth - 1; // volcanoMap[0..bottomLim] are volcanoes on the bottom row
        int rightLim = bottomLim + (this.boardHeight - 2); // volcanoMap[bottomLim..rightLim] are volcanoes on the right
        int topLim = rightLim + this.boardWidth; // same logic
        // leftLim is just whatever that haven't been iterated across

        for (int i = 0; i < volcanoMap.length; i++ ){
            AnimalType volcanoType = volcanoMap[i];
            Texture volcanoSprite = assets.getSprite(volcanoType.volcanoSpriteName);
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
            Coordinate coor = positionCoorMap.get(currCave.location);
            Texture sprite = FieryDragonAssets.getInstance().getSprite(currCave.type.caveSpriteName);

            float x = coor.x;
            float y = coor.y;

            if (currCave.location <= bottomLim) {
                y -= sprite.getHeight() + GUTTER_PX_SIZE;
            } else if (currCave.location <= rightLim ) {
                x += sprite.getWidth() + GUTTER_PX_SIZE;
            } else if (currCave.location <= topLim) {
                y += sprite.getHeight() + GUTTER_PX_SIZE;
            } else {
                x -= sprite.getWidth() + GUTTER_PX_SIZE;
            }

            caveCoorMap.put(currCave, new Coordinate(x,y));
            batch.draw(sprite, x,y);
        }

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawVolcanoes(batch);
        drawCaves(batch);
        drawPlayers(batch);
    }


}
