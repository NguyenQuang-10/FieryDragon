package com.mygdx.game.FieryDragonImplementations;

import com.mygdx.game.FieryDragonPrototype.*;

public class MovementChitCard extends ChitCard {
    public MovementChitCard(AnimalType type, int animalCount, FieryDragonBoard board, ChitCardManager manager) {
        super(type, animalCount, board, manager);
    }

    @Override
    public void handleSelection() {
        if (!flipped) {

            AbstractPlayer activePlayer = manager.getActivePlayer();
            AnimalType playerVolcano = board.getPlayerVolcano(activePlayer);

            board.movePlayerBy(activePlayer, animalCount);

        }

        super.handleSelection();
    }
}
