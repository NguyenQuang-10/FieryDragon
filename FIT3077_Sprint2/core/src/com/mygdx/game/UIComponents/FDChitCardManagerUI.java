package com.mygdx.game.UIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.Board.Board;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.ChitCards.ChitCard;
import com.mygdx.game.ChitCards.ChitCardManager;

import java.util.HashMap;

public class FDChitCardManagerUI extends Group {

    final private ChitCardManager manager;
    final private int maxWidth;
    final private int maxHeight;
    final private HashMap<ChitCard, Texture> chitCardSprites = new HashMap<>();

    public FDChitCardManagerUI(float x, float y,
                               int maxWidth, int maxHeight,
                               Board board,
                               ChitCardManager manager) {
        this.manager = manager;
        manager.generateChitCards(board);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        setX(x);
        setY(y);

        loadChitCardSprite();
        initChitCardUI();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private void initChitCardUI() {
        int GUTTER_PX = 5;
        ChitCard[] chitCards = manager.getChitCards();
        int currRow = 1;
        int currCol = 1;
        float xOffset = getX();
        float yOffset = getY();
        for (ChitCard chit : chitCards) {
            Texture sprite = chitCardSprites.get(chit);
            FDChitCardUI actor = new FDChitCardUI(xOffset, yOffset, sprite, chit);
            addActor(actor);
            if (currCol < maxWidth) {
                xOffset += sprite.getWidth() + GUTTER_PX;
                currCol += 1;
            } else if (currRow < maxHeight) {
                xOffset = getX();
                yOffset += sprite.getHeight() + GUTTER_PX;

                currRow += 1;
                currCol = 1;

            } else {
                break;
            }
        }

    }

    private void loadChitCardSprite() {
        ChitCard[] chitCards = manager.getChitCards();
        HashMap<AnimalType, String> filePrefixes = new HashMap<>();
        filePrefixes.put(AnimalType.BABY_DRAGON, "babyDragon");
        filePrefixes.put(AnimalType.SALAMANDER, "sala");
        filePrefixes.put(AnimalType.BAT, "bat");
        filePrefixes.put(AnimalType.SPIDER, "spider");
        filePrefixes.put(AnimalType.PIRATE_DRAGON, "pirateDragon");

        for (ChitCard chit : chitCards) {
            String filePrefix = filePrefixes.get(chit.getType());
            int count = chit.getAnimalCount();

            String fileName = String.format("Chit\\%s%d.png", filePrefix, count);
            Texture sprite = new Texture(Gdx.files.internal(fileName));
            chitCardSprites.put(chit, sprite);
        }
    }
}
