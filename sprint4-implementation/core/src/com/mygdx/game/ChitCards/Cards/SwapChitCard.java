package com.mygdx.game.ChitCards.Cards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;
import com.mygdx.game.ChitCards.AnimalType;
import com.mygdx.game.ChitCards.ChitCard;
import com.mygdx.game.ChitCards.ChitCardManager;
import com.mygdx.game.ChitCards.ITurnManager;

public class SwapChitCard extends ChitCard {
    public SwapChitCard(Board board, ITurnManager manager) {
        super(AnimalType.SWAP, 0, board, manager);
    }

    @Override
    public void handleSelection() {
        Player activePlayer = manager.getActivePlayer();
        if (!flipped) {
            flipped = true;
            if (!activePlayer.isInCave) {
                Player[] players = manager.getAllPlayers();
                int activePlayerPosition = board.getPlayerPosition(activePlayer);

                // find the player that is closest to the active player
                Player closestPlayer = null;
                int closestDistance = 9999; // will get overwrite in the for loop when looking for the closest player
                for (Player p : players) {
                    if (p != activePlayer && !p.isInCave) {
                        int otherPlayerPosition = board.getPlayerPosition(p);

                        int distanceFromActivePlayer = board.getDistanceBetween(activePlayerPosition, otherPlayerPosition);

                        if (closestPlayer == null || Math.abs(closestDistance) > Math.abs(distanceFromActivePlayer)) {
                            closestPlayer = p;
                            closestDistance = distanceFromActivePlayer;
                        }
                    }
                }

                if (closestPlayer != null) {
                    // move player by 1 so that they don't block the tile/slot that the other player needs to go to
                    board.movePlayerBy(activePlayer, -1);
                    board.movePlayerBy(closestPlayer, -1);

                    // move activePlayer to the original position of the closest player
                    board.movePlayerBy(activePlayer, closestDistance + 1);

                    // move the closest player to the original position of the active player
                    board.movePlayerBy(closestPlayer, -closestDistance + 1);
                }
            }
            manager.endTurn();
        }
    }
}
