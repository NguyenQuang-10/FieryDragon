package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Player;

public interface ITurnManager {
    public void endTurn();
    public Player getActivePlayer();
}
