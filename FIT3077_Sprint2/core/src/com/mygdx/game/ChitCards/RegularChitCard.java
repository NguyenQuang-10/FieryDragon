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

        System.out.println();
        System.out.print("Player volcano: ");
        System.out.println(board.getPlayerVolcano(activePlayer));
        System.out.print("Card type: ");
        System.out.println(type);
        System.out.print("Card number: ");
        System.out.println(animalCount);

        if (type == board.getPlayerVolcano(activePlayer)) {
            board.movePlayerBy(activePlayer, animalCount);
        }
    }
}
