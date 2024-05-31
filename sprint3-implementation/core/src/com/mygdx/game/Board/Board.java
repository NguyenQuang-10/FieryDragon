package com.mygdx.game.Board;


import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ChitCards.AnimalType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.stream.Collectors;

// Class managing state relating to the FieryDragon Board
// Responsibility includes
// - moving Player
// - ending the game
// - initiating and keeping track of board arrangement
public class Board {
     // map linking player to the cave that belongs to them
     Map<Player, Cave> playerCave = new LinkedHashMap<>();

     // map linking player to the cave that belongs to the
     Map<Player, Integer> playerDistanceFromCave = new HashMap<>();

     // map linking player to the cave that belongs to them
     Map<Player, Integer> playerPositions = new LinkedHashMap<>();

     // an array of AnimalType that represent type of volcano squares on the board
     // volcanoMap[i] is the type of volcano at location i on the board
     AnimalType[] volcanoMap;
     private Timer gameTimer;
     boolean timeLimitReached = false;

     // length of the board
     int length;
     // there has been a winner if this is true
     boolean gameEnded = false;

     // configure initial board state
     /*
       @param players - players partaking in the game
       @param volcanoMap - array of AnimalType that describe the board topology
     */
     public Board(Player[] players, String mode, Map<String, AnimalType> animalTypeMap) {
          // create yaml object and instantiate variables
          Yaml yaml = new Yaml();
          List<String> boardData = new ArrayList<>();
          List<String> playerPosition = new ArrayList<>();
          try {
               // load data from yaml file into a Map object
               InputStream inputStream = Files.newInputStream(Paths.get("save_file.yaml"));
               Map<String, List<String>> yamlData = yaml.load(inputStream);
               inputStream.close();

               // get boardData and playerPosition
               if (mode.equals("default")) {
                    boardData = yamlData.get("boardDefault");

               } else {
                    boardData = yamlData.get("boardCustom");
                    playerPosition = yamlData.get("playerPositionCustom");
               }

          } catch(Exception e) {
               System.out.print(e.getMessage());
          }




          // render the board using VolcanoCard
          AnimalType[] volcanoPosition = new AnimalType[boardData.size()];
          for (int i = 0; i < boardData.size() / 3; i++) {
               AnimalType animalType1 = animalTypeMap.get(boardData.get(i * 3));
               AnimalType animalType2 = animalTypeMap.get(boardData.get(i * 3 + 1));
               AnimalType animalType3 = animalTypeMap.get(boardData.get(i * 3 + 2));
               if (mode.equals("default")) {
                    VolcanoCard volcanoCard = new VolcanoCard(animalType1, animalType2, animalType3);

                    Array<AnimalType> animalTypes = volcanoCard.getTypes();
                    volcanoPosition[i*3] = animalTypes.get(0);
                    volcanoPosition[i*3+1] = animalTypes.get(1);
                    volcanoPosition[i*3+2] = animalTypes.get(2);
               } else {
                    volcanoPosition[i*3] = animalType1;
                    volcanoPosition[i*3+1] = animalType2;
                    volcanoPosition[i*3+2] = animalType3;
               }

          }

          // set VolcanoMap and Player
          setVolcanoMap(volcanoPosition);
          if (mode.equals("default")) {
               setPlayers(players, null);
          } else if (mode.equals("custom")) {
               setPlayers(players, playerPosition);
          }
          startGameTimer();
     }
     private void startGameTimer() {
          gameTimer = new Timer();
          gameTimer.schedule(new TimerTask() {
               @Override
               public void run() {
                    endGame();
               }
// the time limit to play the game was 3 hours
          }, 648000000);
     }
     public void endGame() {
          // Cancel the timer
          gameTimer.cancel();
          // Set the gameEnded flag to true
          gameEnded = true;
          // Print the time limit message
          timeLimitReached = true;
     }
     public boolean hasTimeLimitReached() {
          return timeLimitReached;
     }

     /*
       @param player - the player to move
       @param moves - number of moves to move player by, from their current position
       Returns true if player moved, false otherwise
     */
     public boolean movePlayerBy(Player player, int moves) {
          // how many moves the player have made/ how far away they are from the cave if the move is made
          int newDistanceFromCave = this.playerDistanceFromCave.get(player) + moves;

          // player position on the board
          int playerPosition = this.playerPositions.get(player);

          // new position on the board, ensures that players loops around the board
          // (i.e. Position after 24 is 0)
          int newPosition = (playerPosition + moves) % this.length;

          if (newPosition < 0) {
               newPosition = this.length - (Math.abs(newPosition) % this.length );
          }

          // the cave that belongs to the current player
          Cave currPlayerCave = playerCave.get(player);

          // if the player is in their cave, move them out
          if (player.isInCave && moves > 0) {
               // perform checks to see if moving to the new position is illegal
               int playerCount = playerCave.size();
               Player[] players = playerCave.keySet().toArray(new Player[playerCount]);

               boolean shouldPerformMove = true;

               // check to see if there is any player block the position this player is moving to
               // prevent move moment if there are
               for (Player p: players) {
                    if (p != player && !p.isInCave && this.playerPositions.get(p) == newPosition - 1) {
                         shouldPerformMove = false;
                         break;
                    }
               }

               if (shouldPerformMove) {
                    player.isInCave = false;
                    newPosition -= 1;
                    this.playerPositions.put(player, newPosition);
                    this.playerDistanceFromCave.put(player, newDistanceFromCave);
               }

               return shouldPerformMove;
          } else if (newDistanceFromCave == this.volcanoMap.length + 2) {
               // if player looped around the board with an exact number of moves, let them in the cave
               // and end the game

               player.isInCave = true;
               this.playerPositions.put(player, currPlayerCave.position);
               gameEnded = true;
               this.playerDistanceFromCave.put(player, newDistanceFromCave);
               return true;
          } else {
               // perform checks to see if moving to the new position is illegal
               int playerCount = playerCave.size();
               Player[] players = playerCave.keySet().toArray(new Player[playerCount]);

               boolean shouldPerformMove = true;

               // check to see if there is any player block the position this player is moving to
               // prevent move moment if there are
               for (Player p: players) {
                    if (p != player && !p.isInCave && this.playerPositions.get(p) == newPosition) {
                         shouldPerformMove = false;
                         break;
                    }
               }

               // prevent moving if you looped around the board don't have the exact move
               // needed to enter the cave
               // and prevent player from moving too far back beyond where they exit the cave (i.e. the player cannot
               // move backward into their cave or beyond it)
               if (newDistanceFromCave < 1 || newDistanceFromCave > this.volcanoMap.length + 2) {
                    shouldPerformMove = false;
               }

               // if not illegal, perform move
               if (shouldPerformMove) {
                    playerPositions.put(player, newPosition);
                    this.playerDistanceFromCave.put(player, newDistanceFromCave);
               }

               return shouldPerformMove;
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
               return volcanoMap[playerPositions.get(player)];
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
     protected void setPlayers(Player[] newPlayers, List<String> position) {
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
               if (position != null) {
                    int currentPosition = Integer.parseInt(position.get(i));
                    if (currentPosition != startLocation) {
                         newPlayers[i].isInCave = false;
                    }
                    this.playerPositions.put(newPlayers[i], currentPosition);
                    this.playerDistanceFromCave.put(newPlayers[i], (currentPosition - startLocation + 24) % 24);
               } else {
                    this.playerPositions.put(newPlayers[i], startLocation);
                    this.playerDistanceFromCave.put(newPlayers[i], 0);
               }
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

     // return the player's position on the board
     public int getPlayerPosition(Player player) {
          return this.playerPositions.get(player);
     }

     // return true if game has a winner and ended, false otherwise
     public boolean hasGameEnded() {
          return gameEnded;
     }

     // save information to yaml file
     public HashMap<String, List<String>> save() {
          HashMap<String, List<String>> map = new HashMap<>();
          map.put("boardCustom", Arrays.stream(volcanoMap).map(Enum::name).collect(Collectors.toList()));
          map.put("playerPositionCustom", Arrays.stream(playerPositions.values().toArray()).map(Object::toString).collect(Collectors.toList()));
          return map;
     }
}
