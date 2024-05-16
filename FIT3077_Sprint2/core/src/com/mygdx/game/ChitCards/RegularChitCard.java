package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

// A chit card that has a Salamander, Baby Dragon, Spider or Bat type
public class RegularChitCard extends ChitCard{
    // Constructor
    public RegularChitCard(AnimalType type, int animalCount, Board board, ChitCardManager manager) {
        super(type, animalCount, board, manager);
    }

    // If player selected this chit card, if the player is standing on a volcano with the same type,
    // move the player by the number of animal on the card
    // else do nothing
    @Override
    public void handleSelection() {
        if (!flipped) {
            flipped = true;
            Player activePlayer = manager.getActivePlayer();

            if (type == board.getPlayerVolcano(activePlayer)) {
                boolean moveSuccessful = board.movePlayerBy(activePlayer, animalCount);
                if (!moveSuccessful) {
                    manager.endTurn();
                }
            } else {
                manager.endTurn();
            }
        }

    }
}
