package com.mygdx.game.ChitCards.Cards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.ChitCards.ChitCard;
import com.mygdx.game.ChitCards.ITurnManager;

public class TrapChitCard extends ChitCard {
    public TrapChitCard(int animalCount, Board board, ITurnManager manager) {
        super(AnimalType.TRAP, animalCount, board, manager);
    }

    @Override
    public void handleSelection() {
        if (!flipped) {
            flipped = true;

            Player activePlayer = manager.getActivePlayer();
            manager.trapPlayerTurn(this.animalCount, activePlayer);

            manager.endTurn();
        }

    }
}
