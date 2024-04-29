package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;

// I think I should make this abstract
public abstract class ChitCard {
    final protected AnimalType type;
    final protected int animalCount;
    protected boolean flipped = false;
    final protected Board board;
    final protected ITurnManager manager;

    public ChitCard(AnimalType type, int animalCount, Board board, ChitCardManager manager) {
        this.type = type;
        this.animalCount = animalCount;
        this.board = board;
        this.manager = manager;
    }

    public void handleSelection(){
        if (!flipped) {
            flipped = true;
        }
    }

    public void resetFlipState() {
        this.flipped = false;
    }
    public AnimalType getType() {return type; }
    public int getAnimalCount() { return  animalCount; }
    public boolean getFlippedState() { return this.flipped; }
}

