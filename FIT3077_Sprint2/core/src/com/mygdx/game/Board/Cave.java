package com.mygdx.game.Board;

import com.mygdx.game.ChitCards.AnimalType;

public class Cave {
    public AnimalType type;
    public int position;

    public Cave(AnimalType type, int position) {
        this.type = type;
        this.position = position;
    }
}
