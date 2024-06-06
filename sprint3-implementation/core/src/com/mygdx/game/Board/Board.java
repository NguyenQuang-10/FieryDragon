package com.mygdx.game.Board;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ChitCards.AnimalType;

import java.util.*;

import org.yaml.snakeyaml.Yaml;
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
     List<VolcanoCard> volcanoCards = new ArrayList<>();

     // an array of AnimalType that represent type of volcano squares on the board
     // volcanoMap[i] is the type of volcano at location i on the board
     AnimalType[] volcanoMap;
     private Timer gameTimer;
     boolean timeLimitReached = false;

     // length of the board
     int length;
     // there has been a winner if this is true
     boolean gameEnded = false;
     int volcanoCardSize = 3;

     protected static AnimalType[] defaultBoardArrangement = {AnimalType.BABY_DRAGON, AnimalType.BAT, AnimalType.SPIDER,
             AnimalType.SPIDER, AnimalType.BAT, AnimalType.SALAMANDER, AnimalType.SALAMANDER, AnimalType.SPIDER,
             AnimalType.BAT, AnimalType.BABY_DRAGON, AnimalType.SALAMANDER, AnimalType.BAT, AnimalType.SPIDER,
             AnimalType.SALAMANDER, AnimalType.BABY_DRAGON, AnimalType.BAT, AnimalType.BABY_DRAGON,
             AnimalType.SALAMANDER, AnimalType.BAT, AnimalType.SPIDER, AnimalType.BABY_DRAGON, AnimalType.SALAMANDER,
             AnimalType.BABY_DRAGON, AnimalType.SPIDER};

     // configure initial board state
     /*
       @param players - players partaking in the game
       @param volcanoMap - array of AnimalType that describe the board topology
     */
     public Board(Player[] players, String mode, Map<String, AnimalType> animalTypeMap) {
          // create yaml object and instantiate variables
          Yaml yaml = new Yaml();
          List<String> boardData;
          List<String> playerPosition = new ArrayList<>();
          List<String> caveDistances = new ArrayList<>();
          List<String> cavePosition = new ArrayList<>();

          FileHandle file = Gdx.files.local("save_file.yaml");
          String yamlString = file.readString();

          Map<String, List<String>> yamlData = yaml.load(yamlString);

          // render the board using VolcanoCard
          AnimalType[] tileTypeMap;

          // get boardData and playerPosition
          if (mode.equals("default")) {
               tileTypeMap = Board.defaultBoardArrangement;

          } else {
               boardData = yamlData.get("boardCustom");
               playerPosition = yamlData.get("playerPositionCustom");
               cavePosition = yamlData.get("cavePosition");
               volcanoCardSize = Integer.parseInt(yamlData.get("volcanoCardSize").get(0));
               caveDistances = yamlData.get("playerDistanceFromCaveCustom");

               tileTypeMap = new AnimalType[boardData.size()];

               for (int i = 0; i < boardData.size(); i++) {
                    tileTypeMap[i] = animalTypeMap.get(boardData.get(i));
               }
          }

          for (int i = 0; i < tileTypeMap.length / volcanoCardSize; i++) {
               AnimalType[] tiles = new AnimalType[volcanoCardSize];
               for (int tileNo = 0; tileNo < volcanoCardSize; tileNo++) {
                    tiles[tileNo] = tileTypeMap[i*volcanoCardSize + tileNo];
               }
               VolcanoCard volcanoCard = new VolcanoCard(tiles);
               volcanoCards.add(volcanoCard);

          }

          // set VolcanoMap and Player
          setVolcanoMap(tileTypeMap);
          if (mode.equals("default")) {
               setPlayers(players, null,null, null);
          } else if (mode.equals("custom")) {
               setPlayers(players, playerPosition,cavePosition, caveDistances);
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
// the time limit to play the game was 5 minutes
          }, 300000);
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

          // if the player is in their cave, move them out if the number of moves is positive
          if (player.isInCave) {
               if (moves <= 0) {
                    return false;
               }

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
                    if (newPosition > 0) {
                         newPosition -= 1;
                    } else {
                         newPosition = volcanoMap.length - 1;
                    }
                    this.playerPositions.put(player, newPosition);
                    this.playerDistanceFromCave.put(player, newDistanceFromCave);
               }

               return shouldPerformMove;
          } else if (newDistanceFromCave == this.volcanoMap.length + 2 && moves > 0) {
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
               if (newDistanceFromCave > this.volcanoMap.length + 2) {
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
     protected void setPlayers(Player[] newPlayers, List<String> position,List<String> cavePosition, List<String> distanceFromCave) {
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
               if (cavePosition != null){
                    int cavePos = Integer.parseInt(cavePosition.get(i));
                    int belongToVolcanoCardAt = cavePos / volcanoCardSize;
                    int indentAtTile = cavePos % volcanoCardSize;

                    VolcanoCard volCard = volcanoCards.get(belongToVolcanoCardAt);
                    volCard.caveAt = indentAtTile;
                    playerCave.put(newPlayers[i], new Cave(randomAniType, cavePos));
               } else {
                    int belongToVolcanoCardAt = startLocation / volcanoCardSize;
                    int indentAtTile = startLocation % volcanoCardSize;

                    VolcanoCard volCard = volcanoCards.get(belongToVolcanoCardAt);
                    volCard.caveAt = indentAtTile;

                    playerCave.put(newPlayers[i], new Cave(randomAniType, startLocation));
               }
               if (position != null && distanceFromCave != null ) {
                    int currentPosition = Integer.parseInt(position.get(i));
                    int caveDistance = Integer.parseInt(distanceFromCave.get(i));
                    if (currentPosition != Integer.parseInt(cavePosition.get(i))) {
                         newPlayers[i].isInCave = false;
                    }
                    this.playerPositions.put(newPlayers[i], currentPosition);
                    this.playerDistanceFromCave.put(newPlayers[i], caveDistance);
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
          List<String> cavePosition = new ArrayList<>();
          for (Cave cave: playerCave.values()){
               cavePosition.add(String.valueOf(cave.position));
          }
          map.put("boardCustom", Arrays.stream(volcanoMap).map(Enum::name).collect(Collectors.toList()));
          map.put("playerPositionCustom", Arrays.stream(playerPositions.values().toArray()).map(Object::toString).collect(Collectors.toList()));
          map.put("playerDistanceFromCaveCustom", Arrays.stream(playerDistanceFromCave.values().toArray()).map(Object::toString).collect(Collectors.toList()));
          map.put("cavePosition",cavePosition);

          List<String> volcanoMapSizeList = new ArrayList<>();
          volcanoMapSizeList.add(Integer.toString(volcanoCardSize));
          map.put("volcanoCardSize", volcanoMapSizeList);
          return map;
     }

     // calculate the min number of moves needed to reach position toPosition from fromPosition
     // @param
     //   fromPosition (int) - the starting position, in the range [0, boardLength]
     //   toPosition (int) - the ending position, in the range [0, boardLength]
     //
     // return as an integer the number of moves needed to make, negative means moving backward,
     // positive mean moving forward
     public int getDistanceBetween(int fromPosition, int toPosition) {
          // how many move backwards needed to reach toPosition from fromPosition
          int backwardDistance;
          if (toPosition < fromPosition) { // e.g. fromPosition is 6 and toPosition is 3 => distance backward is -3
               backwardDistance = toPosition - fromPosition;
          } else { // e.g. fromPosition is 3 and toPosition is 22 with board length of 24 (rmb that the last slot (23) is followed by slot 0)
                   // the distance backward is -5, fromPosition to slot 0 is 3 move backwards, slot 0 to slot 22 is -2 slot backwards
               backwardDistance = -fromPosition + (toPosition - volcanoMap.length);
          }


          // how many move forward needed to reach toPosition from fromPosition
          int forwardDistance;
          if (toPosition > fromPosition) { // e.g. fromPosition is 3 and toPosition is 6 => distance forward is 3
               forwardDistance = toPosition - fromPosition;
          } else { // e.g. fromPosition is 22 and toPosition is 3 with board length of 24 (rmb that the last slot (23) is followed by slot 0)
               // the distance forward is 5, fromPosition to slot 0 is 2 move forward, slot 0 to slot 3 is 3 slot forward
               forwardDistance = (volcanoMap.length - fromPosition) + toPosition;
          }

          if (Math.abs(backwardDistance) < forwardDistance) {
               return backwardDistance;
          } else {
               return forwardDistance;
          }

     }
}
