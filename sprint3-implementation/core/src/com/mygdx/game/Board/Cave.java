package com.mygdx.game.Board;

import com.mygdx.game.ChitCards.AnimalType;

// Class used for encapsulating data relating to Cave cards
public class Cave {
    // the type of the cave
    public AnimalType type;
    // the position that it is located on the board
    public int position;

    public Cave(AnimalType type, int position) {
        this.type = type;
        this.position = position;
    }
}
