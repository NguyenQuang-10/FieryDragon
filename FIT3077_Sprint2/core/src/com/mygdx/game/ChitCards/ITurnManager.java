package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Player;

// Interface that describe a class that can manage turn for the game
public interface ITurnManager {
    // end this turn and move on to the turn for the next player
    public void endTurn();
    // get the player whose turn it is
    public Player getActivePlayer();
}
