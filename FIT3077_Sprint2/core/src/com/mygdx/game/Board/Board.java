package com.mygdx.game.Board;

import com.mygdx.game.ChitCards.AnimalType;

// turn this into an abstract class
public interface Board {
     void movePlayerBy(Player player, int moves);
     AnimalType getPlayerVolcano(Player player);
     Cave getPlayerCave(Player player);
     Player[] getPlayers();
     AnimalType[] getVolcanoMap();
     Cave[] getCaves();
     boolean hasGameEnded();
}
