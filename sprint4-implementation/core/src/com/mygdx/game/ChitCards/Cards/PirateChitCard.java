package com.mygdx.game.ChitCards.Cards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.ChitCards.ChitCard;
import com.mygdx.game.ChitCards.ChitCardManager;

// A chit card that has a Pirate Dragon type
public class PirateChitCard extends ChitCard {
    // Constructor
    public PirateChitCard(int animalCount, Board board, ChitCardManager manager) {
        super(AnimalType.PIRATE_DRAGON, animalCount, board, manager);
    }

    // If selected, move the player back the same number as animalCount
    @Override
    public void handleSelection() {
        if (!flipped) {
            flipped = true;
            Player activePlayer = manager.getActivePlayer();
            board.movePlayerBy(activePlayer, -1 * animalCount);

            manager.endTurn();
        }
    }
}
