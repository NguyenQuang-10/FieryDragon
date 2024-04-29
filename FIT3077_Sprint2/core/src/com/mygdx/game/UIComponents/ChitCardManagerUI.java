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

// Retrieves state from ChitCardManager and render to the screen base on those state
// Orders and render ChitCard(s) into a grid
// View for ChitCardManager class
public class ChitCardManagerUI extends Group {

    // Controller: ChitCardManager instance
    final private ChitCardManager manager;

    // max number of chit cards in a row
    final private int maxWidth;

    // max number of rows of chit cards
    final private int maxHeight;
    // maps ChitCard instance to their corresponding sprite
    final private HashMap<ChitCard, Texture> chitCardSprites = new HashMap<>();

    // Constructor
    public ChitCardManagerUI(float x, float y,
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

    // See libGDX documentation
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    // Create ChitCardUI views for every ChitCard instance belonging to the associated ChitCardManager
    // Also order them by setting their coordinates
    private void initChitCardUI() {
        int GUTTER_PX = 5;
        ChitCard[] chitCards = manager.getChitCards();
        int currRow = 1;
        int currCol = 1;
        float xOffset = getX();
        float yOffset = getY();
        for (ChitCard chit : chitCards) {
            Texture sprite = chitCardSprites.get(chit);
            ChitCardUI actor = new ChitCardUI(xOffset, yOffset, sprite, chit);
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

    // load sprites for chit cards
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
