package com.mygdx.game.UIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ChitCards.ChitCard;
import com.mygdx.game.ChitCards.ITurnManager;

// Retrieves state from ChitCard and render to the screen base on those state
// View for ChitCard class
public class ChitCardUI extends FieryDragonUI {
    // default hidden side of chit card sprite
    private Texture hiddenSprite = new Texture(Gdx.files.internal("Chit\\back.png"));
    // the sprite of the flipped side of the chitCard
    final private Texture flippedSprite;
    // chit card instance/controller
    private final ChitCard chitCard;
    private boolean clickedOn = false;
    private boolean lastFlipState = false;

    // constructor
    public ChitCardUI(float x, float y, Texture sprite , ChitCard chitCard, ITurnManager turnManager){
        super(turnManager);
        this.flippedSprite = sprite;
        this.chitCard = chitCard;

        this.hiddenSprite = flippedSprite;

        // see libGDX documentation
        setBounds(x, y, hiddenSprite.getWidth(), hiddenSprite.getHeight());
        // see libGDX documentation, listen for user click
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                clickedOn = true;
            }
        });
    }

    // see libGDX documentation, draws the chit card
    @Override
    public void draw(Batch batch, float parentAlpha) {
//        System.out.println(waitTimeLeft);
        if (!isWaitingForTurnEnd()) {
            if (chitCard.getFlippedState()) {
                batch.draw(flippedSprite, getX(), getY());
            } else {
                batch.draw(hiddenSprite, getX(), getY());
            }
        } else {
            if (lastFlipState) {
                batch.draw(flippedSprite, getX(), getY());
            } else {
                batch.draw(hiddenSprite, getX(), getY());
            }
        }

        super.draw(batch, parentAlpha);
    }

    @Override
    public void endTurnChange() {
        super.endTurnChange();
        lastFlipState = chitCard.getFlippedState();
    }

    @Override
    public void act(float delta) {
        if (clickedOn) {
            if (!isWaitingForTurnEnd()) {
                lastFlipState = true;
                chitCard.handleSelection();
            }
            clickedOn = false;
        }

        super.act(delta);
    }
}
