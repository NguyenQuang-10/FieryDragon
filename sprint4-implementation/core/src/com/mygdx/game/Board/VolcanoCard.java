package com.mygdx.game.Board;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ChitCards.AnimalType;

public class VolcanoCard {
    private final Array<AnimalType> types = new Array<>();
    protected int caveAt;


    public VolcanoCard(AnimalType[] types) {
        for (AnimalType boardSlot : types) {
            this.types.add(boardSlot);
        }
    }

    // randomly determine the tile order whether the volcano card is flipped or not
    public Array<AnimalType> getTypes() {
        if (Math.random() < 0.5) {
            this.types.reverse();
            return this.types;
        } else {
            return this.types;
        }
    }

    public int getIndentPosition() {
        return caveAt;
    }
}

