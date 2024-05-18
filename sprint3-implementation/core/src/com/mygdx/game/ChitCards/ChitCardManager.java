package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

import java.util.ArrayList;
import java.util.Collections;

// A class that contains a collection of all chit card in the game
// allowing it to manage turns
public class ChitCardManager implements ITurnManager {
    // players in the game
    protected Player[] players;

    // chit cards that are contained in the game
    protected ChitCard[] chitCards;

    // the index of the player whose turn it is in this.players
    int activePlayerIndex; // index of players whose turn belong to

    // Constructor
    public ChitCardManager(Player[] players){
        this.players = players;
        this.activePlayerIndex = 0;
    }

    // Generate 16 chit card that follows the base game rules
    /*
        @param board - The board that will be associated with all chit cards
     */
    public void generateChitCards(Board board) {
        ArrayList<ChitCard> cards = new ArrayList<>();
        AnimalType[] animalTypes =  AnimalType.values();

        for (AnimalType animalType : animalTypes) {
            if (animalType != AnimalType.PIRATE_DRAGON) {
                for (int aCount = 1; aCount <= 3; aCount++) {
                    ChitCard card = new RegularChitCard(animalType, aCount, board, this);
                    cards.add(card);
                }
            }
        }

        for (int i = 1; i <= 2; i++) {
            cards.add(new PirateChitCard(i, board, this));
            cards.add(new PirateChitCard(i, board, this));
        }

        Collections.shuffle(cards);
        chitCards = new ChitCard[16];
        chitCards = cards.toArray(chitCards);
    }

    // Perform operations to end the turn by resetting all chit cards
    public void endTurn() {
        this.activePlayerIndex = (activePlayerIndex + 1) % this.players.length;
        for (ChitCard chitCard : chitCards) {
            chitCard.resetFlipState();
        }
    }

    // see ITurnManager
    public Player getActivePlayer() {
        return this.players[activePlayerIndex];
    }

    // get the array of every chit card in the game.
    public ChitCard[] getChitCards() { return this.chitCards; }
}
