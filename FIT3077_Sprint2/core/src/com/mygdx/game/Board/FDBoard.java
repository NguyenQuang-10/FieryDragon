package com.mygdx.game.Board;


import com.mygdx.game.ChitCards.AnimalType;

import java.util.*;


// needs to be a singleton
public class FDBoard implements Board {
    Map<Player, Cave>playerCave = new HashMap<>();
    AnimalType[] volcanoMap;
    int length;
    boolean gameEnded = false;

    public FDBoard(Player[] players, AnimalType[] volcanoMap){
        setVolcanoMap(volcanoMap);
        setPlayers(players);

        // TODO: REMOVE THIS
        players[1].isInCave = false;
        players[1].position = 13;
    }

    public void movePlayerBy(Player player, int moves) {
        int newPosition = (player.position + moves) % this.length;

        if (newPosition < 0) {
            newPosition = this.length - (Math.abs(newPosition) % this.length );
        }

        Cave currPlayerCave = playerCave.get(player);
        System.out.println(newPosition);
        if (player.isInCave && moves > 0) {
            player.isInCave = false;
            newPosition -= 1;
            player.position = newPosition;
        } else if (player.position < currPlayerCave.position && newPosition == currPlayerCave.position) {
            player.isInCave = true;
            player.position = currPlayerCave.position;
        } else {
            int playerCount = playerCave.size();
            Player[] players = playerCave.keySet().toArray(new Player[playerCount]);

            boolean shouldPerformMove = true;

            for (Player p: players) {
                if (p != player && !p.isInCave && p.position == newPosition) {
                    shouldPerformMove = false;
                    break;
                }
            }

            int unmappedPlayerPosition = player.position + moves;
            if (player.position < currPlayerCave.position &&  unmappedPlayerPosition > currPlayerCave.position) {
                shouldPerformMove = false;
            }

            System.out.println(unmappedPlayerPosition);
            if (unmappedPlayerPosition < currPlayerCave.position) {
                shouldPerformMove = false;
            }

            if (shouldPerformMove) {
                player.position = newPosition;
            }
        }
    }
    public AnimalType getPlayerVolcano(Player player) {
        if (player.isInCave) {
            return playerCave.get(player).type;
        } else {
            return volcanoMap[player.position];
        }
    }

    @Override
    public Cave getPlayerCave(Player player) {
        return playerCave.get(player);
    }

    protected void setVolcanoMap(AnimalType[] volcanoMap) {
        this.volcanoMap = volcanoMap;
        this.length = volcanoMap.length; // 0-indexed
    }
    protected void setPlayers(Player[] newPlayers) {
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

    public AnimalType[] getVolcanoMap() {
        return this.volcanoMap;
    }

    public Cave[] getCaves() {
        int caveCount = playerCave.size();
        return playerCave.values().toArray(new Cave[caveCount]);
    }

    public Player[] getPlayers() {
        int playerCount = playerCave.size();
        return playerCave.keySet().toArray(new Player[playerCount]);
    }

    public boolean hasGameEnded() {
        return gameEnded;
    }
}
