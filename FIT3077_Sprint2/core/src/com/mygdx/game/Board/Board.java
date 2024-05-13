package com.mygdx.game.Board;


import com.mygdx.game.ChitCards.AnimalType;

import java.util.*;


// Class managing state relating to the FieryDragon Board
// Responsbility includes
// - moving Player
// - ending the game
// - initiating and keeping track of board arrangement
public class Board {
     // map linking player to the cave that belongs to them
     Map<Player, Cave>playerCave = new LinkedHashMap<>();
     // an array of AnimalType that respresent type of volcano squares on the board
     // volcanoMap[i] is the type of volcano at location i on the board
     AnimalType[] volcanoMap;

     // length of the board
     int length;
     // there has been a winner if this is true
     boolean gameEnded = false;

     // configure initial board state
     /*
       @param players - players partaking in the game
       @param volcanoMap - array of AnimalType that describe the board topology
     */
     public Board(Player[] players, AnimalType[] volcanoMap){

          setVolcanoMap(volcanoMap);
          setPlayers(players);
     }

     /*
       @param player - the player to move
       @param moves - number of moves to move player by, from their current position
     */
     public void movePlayerBy(Player player, int moves) {


          // new position on the board, ensures that players loops around the board
          // (i.e Position after 24 is 0)
          int newPosition = (player.position + moves) % this.length;

          if (newPosition < 0) {
               newPosition = this.length - (Math.abs(newPosition) % this.length );
          }

          // the cave that belongs to the current player
          Cave currPlayerCave = playerCave.get(player);

          // if the player is in their cave, move them out
          if (player.isInCave && moves > 0) {
               player.isInCave = false;
               newPosition -= 1;
               player.position = newPosition;
          } else if (player.position < currPlayerCave.position && newPosition == currPlayerCave.position) {
               // if player looped around the board with an exact number of moves, let them in the cave
               // and end the game

               player.isInCave = true;
               player.position = currPlayerCave.position;
          } else {
               // perform checks to see if moving to the new position is illegal
               int playerCount = playerCave.size();
               Player[] players = playerCave.keySet().toArray(new Player[playerCount]);

               boolean shouldPerformMove = true;

               // check to see if there is any player block the position this player is moving to
               // prevent move moment if there are
               for (Player p: players) {
                    if (p != player && !p.isInCave && p.position == newPosition) {
                         shouldPerformMove = false;
                         break;
                    }
               }

               // prevent moving if you looped around the board don't have the exact move
               // needed to enter the cave
               int unmappedPlayerPosition = player.position + moves;
               if (player.position < currPlayerCave.position &&  unmappedPlayerPosition > currPlayerCave.position) {
                    shouldPerformMove = false;
               }

               // prevents moving backward pass your cave
               if (player.position >= currPlayerCave.position && moves < 0 && unmappedPlayerPosition < currPlayerCave.position) {
                    shouldPerformMove = false;
               }

               // if not illegal, perform move
               if (shouldPerformMove) {
                    player.position = newPosition;
               }
          }
     }

     // Get the type of the volcano/cave a player is standing on
     /*
          @param player - the player to get this type from
      */
     public AnimalType getPlayerVolcano(Player player) {
          if (player.isInCave) {
               return playerCave.get(player).type;
          } else {
               return volcanoMap[player.position];
          }
     }

     // Get the cave belonging to a player
     /*
          @param player - the player to get this cave from
      */
     public Cave getPlayerCave(Player player) {
          return playerCave.get(player);
     }

     // Initialize board topology based on array of AnimalType
     /*
          @param volcanoMap - the array of AnimalType to get topology information from
      */
     protected void setVolcanoMap(AnimalType[] volcanoMap) {
          this.volcanoMap = volcanoMap;
          this.length = volcanoMap.length; // 0-indexed
     }

     // Initialize Cave for each player and set the players participating in the game
     /*
          @param - newPlayers: array of Player object participating in the game
      */
     protected void setPlayers(Player[] newPlayers) {
          int playerCount = newPlayers.length;

          // evenly spaces caves out on the board with randomised animal types
          int spacing = this.length / playerCount;
          AnimalType[] allowedCaveTypes = {AnimalType.BAT, AnimalType.BABY_DRAGON, AnimalType.SALAMANDER, AnimalType.SPIDER};
          List<AnimalType> types = Arrays.asList(allowedCaveTypes);
          Collections.shuffle(types);

          // generating a cave for each player
          // also randomizing the cave each player starts out on
          for(int i = 0; i < playerCount; i++){
               int startLocation = spacing * (i + 1) - 1;

               AnimalType randomAniType = types.get(i % playerCount);
               playerCave.put(newPlayers[i], new Cave(randomAniType, startLocation));
               newPlayers[i].position = startLocation;
          }
     }

     // get the volcano map
     public AnimalType[] getVolcanoMap() {
          return this.volcanoMap;
     }

     // get Cave object on the board, in the order that they were added in
     public Cave[] getCaves() {
          int caveCount = playerCave.size();
          return playerCave.values().toArray(new Cave[caveCount]);
     }

     // get all Player object on the board, in the order that they were added in
     // the order is the same as the player array used when instantiating.
     public Player[] getPlayers() {
          int playerCount = playerCave.size();
          return playerCave.keySet().toArray(new Player[playerCount]);
     }

     // return true if game has a winner and ended, false otherwise
     public boolean hasGameEnded() {
          return gameEnded;
     }
}
