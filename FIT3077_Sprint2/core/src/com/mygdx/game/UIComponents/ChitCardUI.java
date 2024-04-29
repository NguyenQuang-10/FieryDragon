package com.mygdx.game.UIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ChitCards.ChitCard;

// Retrieves state from ChitCard and render to the screen base on those state
// View for ChitCard class
public class ChitCardUI extends Actor {
    // default hidden side of chit card sprite
    private Texture hiddenSprite = new Texture(Gdx.files.internal("Chit\\back.png"));
    // the sprite of the flipped side of the chitCard
    private Texture flippedSprite;
    // chit card instance/controller
    private ChitCard chitCard;

    // constructor
    public ChitCardUI(float x, float y, Texture sprite , ChitCard chitCard){
        this.flippedSprite = sprite;
        this.chitCard = chitCard;

        // see libGDX documentation
        setBounds(x, y, hiddenSprite.getWidth(), hiddenSprite.getHeight());

        // see libGDX documentation, listen for user click
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                chitCard.handleSelection();
            }
        });
    }

    // see libGDX documentation, draws the chit card
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (chitCard.getFlippedState()) {
            batch.draw(flippedSprite, getX(), getY());
        } else {
            batch.draw(hiddenSprite, getX(), getY());
        }

        super.draw(batch, parentAlpha);
    }
}
