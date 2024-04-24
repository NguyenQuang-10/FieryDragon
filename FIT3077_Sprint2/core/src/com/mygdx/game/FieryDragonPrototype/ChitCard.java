package com.mygdx.game.FieryDragonPrototype;

// I think I should make this abstract
public abstract class ChitCard {
    private String name;
    final protected AnimalType type;
    final protected int animalCount;
    protected boolean flipped = false;
    final protected Board board;
    final protected ChitCardManager manager;

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
}

