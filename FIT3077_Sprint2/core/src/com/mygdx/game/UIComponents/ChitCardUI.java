package com.mygdx.game.UIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ChitCards.ChitCard;

public class ChitCardUI extends Actor {
    private Texture sprite;
    private ChitCard chitCard;

    public ChitCardUI(float x, float y, Texture sprite, ChitCard chitCard){
        this.sprite = sprite;
        this.chitCard = chitCard;

        setBounds(x, y, sprite.getWidth(), sprite.getHeight());

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                chitCard.handleSelection();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY());
        super.draw(batch, parentAlpha);
    }
}
