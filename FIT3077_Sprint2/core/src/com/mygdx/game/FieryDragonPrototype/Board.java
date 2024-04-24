package com.mygdx.game.FieryDragonPrototype;

// turn this into an abstract class
public interface Board {
    public void init(AbstractPlayer[] players, AnimalType[] volcanoMap);
    public void movePlayerBy(AbstractPlayer player, int moves);
    public AnimalType getPlayerVolcano(AbstractPlayer player);
    public Cave getPlayerCave(AbstractPlayer player);
    public AbstractPlayer[] getPlayers();
    public AnimalType[] getVolcanoMap();
    public Cave[] getCaves();
    public boolean hasGameEnded();
}
