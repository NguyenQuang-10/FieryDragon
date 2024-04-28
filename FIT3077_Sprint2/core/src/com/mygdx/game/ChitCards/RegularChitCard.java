package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

public class RegularChitCard extends ChitCard{
    public RegularChitCard(AnimalType type, int animalCount, Board board, ChitCardManager manager) {
        super(type, animalCount, board, manager);
    }

    @Override
    public void handleSelection() {
        super.handleSelection();

        Player activePlayer = manager.getActivePlayer();

        if (type == board.getPlayerVolcano(activePlayer)) {
            board.movePlayerBy(activePlayer, animalCount);
        }
    }
}
