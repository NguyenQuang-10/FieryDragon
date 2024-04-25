package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

public class PirateChitCard extends ChitCard{
    public PirateChitCard(int animalCount, Board board, ChitCardManager manager) {
        super(AnimalType.PIRATE_DRAGON, animalCount, board, manager);
    }

    @Override
    public void handleSelection() {
        super.handleSelection();

        Player activePlayer = manager.getActivePlayer();
        board.movePlayerBy(activePlayer, -1 * animalCount);

    }
}
