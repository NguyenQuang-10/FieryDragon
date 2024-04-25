package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

import java.util.ArrayList;
import java.util.Collections;


public class FDChitCardManager extends ChitCardManager{
    public FDChitCardManager(Player[] players) {
        super(players);
    }

    @Override
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
}
