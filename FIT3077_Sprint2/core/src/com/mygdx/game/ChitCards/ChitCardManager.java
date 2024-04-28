package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

import java.util.ArrayList;
import java.util.Collections;

public class ChitCardManager implements ITurnManager {
    protected Player[] players;
    protected ChitCard[] chitCards;
    int activePlayerIndex; // index of players whose turn belong to

    public ChitCardManager(Player[] players){
        this.players = players;
        this.activePlayerIndex = 0;
    }

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
            ChitCard card = new PirateChitCard(i, board, this);
            cards.add(card);
            cards.add(card);
        }

        Collections.shuffle(cards);
        chitCards = new ChitCard[16];
        chitCards = cards.toArray(chitCards);
    }

    public void endTurn() {
        this.activePlayerIndex = (activePlayerIndex + 1) % this.players.length;
        for (ChitCard chitCard : chitCards) {
            chitCard.resetFlipState();
        }
    }

    public Player getActivePlayer() {
        return this.players[activePlayerIndex];
    }
    public ChitCard[] getChitCards() { return this.chitCards; }
}
