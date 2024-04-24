package com.mygdx.game.FieryDragonPrototype;


import java.util.*;


// needs to be a singleton
public class FieryDragonBoard implements  Board {
    Map<AbstractPlayer, Cave>playerCave = new HashMap<AbstractPlayer, Cave>();
    AnimalType[] volcanoMap;
    int length;
    boolean gameEnded = false;

    public FieryDragonBoard(){

    }

    public void init(AbstractPlayer[] players, AnimalType[] volcanoMap) {
        this.setVolcanoMap(volcanoMap);
        this.setPlayers(players);
    }

    public void movePlayerBy(AbstractPlayer player, int moves) {
        int newLocation = player.position + moves;
        if (newLocation < this.length) {
            player.position = newLocation;
        } else if (newLocation == this.length) { // player circled the whole volcano map
            player.isInCave = true;
            gameEnded = true;
        }
    }
    public AnimalType getPlayerVolcano(AbstractPlayer player) {
        return volcanoMap[player.position];
    }

    @Override
    public Cave getPlayerCave(AbstractPlayer player) {
        return playerCave.get(player);
    }

    private void setVolcanoMap(AnimalType[] volcanoMap) {
        this.volcanoMap = volcanoMap;
        this.length = volcanoMap.length; // 0-indexed
    }
    private void setPlayers(AbstractPlayer[] newPlayers) {
        int playerCount = newPlayers.length;

        // evenly spaces caves out on the board with randomised animal types
        int spacing = this.length / playerCount;
        AnimalType[] allowedCaveTypes = {AnimalType.BAT, AnimalType.BABY_DRAGON, AnimalType.SALAMANDER, AnimalType.SPIDER};
        List<AnimalType> types = Arrays.asList(allowedCaveTypes);
        Collections.shuffle(types);

        for(int i = 0; i < playerCount; i++){
            int startLocation = spacing * (i + 1) - 1;

            AnimalType randomAniType = types.get(i % playerCount);
            playerCave.put(newPlayers[i], new Cave(randomAniType, startLocation));
            newPlayers[i].position = startLocation;
        }
    }

    @Override
    public AnimalType[] getVolcanoMap() {
        return this.volcanoMap;
    }

    @Override
    public Cave[] getCaves() {
        int caveCount = playerCave.size();
        return playerCave.values().toArray(new Cave[caveCount]);
    }

    public AbstractPlayer[] getPlayers() {
        int playerCount = playerCave.size();
        return playerCave.keySet().toArray(new AbstractPlayer[playerCount]);
    }

    @Override
    public boolean hasGameEnded() {
        return gameEnded;
    }
}
