package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;

// Class responsible for managing state relating to each chit card in the game
// Responsibility includes
// - knowing if a chit card is flipped
// - what type and how many animal is on a chit card
// - communicating with other class when it is selected so they can
// update their state accordingly
public abstract class ChitCard {
    // the type of the chit card
    final protected AnimalType type;
    // the number of animal on the chit card
    final protected int animalCount;
    // is the card currently flipped? (side showing animal exposed)
    protected boolean flipped = false;
    // the board associated with this chit card
    final protected Board board;

    // turn manager for this chit card
    final protected ITurnManager manager;

    // Constructor
    public ChitCard(AnimalType type, int animalCount, Board board, ChitCardManager manager) {
        this.type = type;
        this.animalCount = animalCount;
        this.board = board;
        this.manager = manager;
    }

    // behaviour that occurs when a chit card is selected
    public void handleSelection(){
        if (!flipped) {
            flipped = true;
        }
    }

    // reset the chit card for the next turn
    public void resetFlipState() {
        this.flipped = false;
    }
    // get the type of the card
    public AnimalType getType() {return type; }
    // get the number of animal on the card
    public int getAnimalCount() { return  animalCount; }
    // check whether or not this card has been flipped/selected
    public boolean getFlippedState() { return this.flipped; }
}

