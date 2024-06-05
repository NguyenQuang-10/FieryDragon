package com.mygdx.game.ChitCards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;
import com.mygdx.game.ChitCards.Cards.PirateChitCard;
import com.mygdx.game.ChitCards.Cards.RegularChitCard;
import com.mygdx.game.ChitCards.Cards.SwapChitCard;
import com.mygdx.game.ChitCards.Cards.TrapChitCard;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

// A class that contains a collection of all chit card in the game
// allowing it to manage turns
public class ChitCardManager implements ITurnManager {
    private final Map<String, AnimalType> animalTypeMap;
    // players in the game
    protected Player[] players;

    // chit cards that are contained in the game
    protected ChitCard[] chitCards;

    // the index of the player whose turn it is in this.players
    int activePlayerIndex; // index of players whose turn belong to
    String mode;
    HashMap<String, List<String>> yamlData;

    // player is trapped if they're turn is skipped because they select a Trap Chit Card
    // this maps the player to the number of turn being skipped
    // if player is not trapped, then the number of turn they have to wait is 0
    private final HashMap<Player, Integer> playerTrappedTurnRemaining = new LinkedHashMap<>();

    // Constructor
    public ChitCardManager(Player[] players, String mode, Map<String, AnimalType> animalTypeMap){
        this.players = players;
        this.activePlayerIndex = 0;
        this.mode = mode;
        this.animalTypeMap = animalTypeMap;

        for (Player p: players) {
            playerTrappedTurnRemaining.put(p, 0);
        }
    }

    // Generate 16 chit card that follows the base game rules
    /*
        @param board - The board that will be associated with all chit cards
     */
    public void generateChitCards(Board board) {
        ArrayList<ChitCard> cards = new ArrayList<>();
        if (mode.equals("default")) {
            // create an array of animalTypes
            AnimalType[] animalTypes = animalTypeMap.values().toArray(new AnimalType[0]);
            EnumSet<AnimalType> regularAnimalTypes = EnumSet.of(AnimalType.BAT, AnimalType.BABY_DRAGON, AnimalType.SPIDER, AnimalType.SALAMANDER);

            // generate the ChitCards sequentially and add regular ChitCard to the board
            for (AnimalType animalType : animalTypes) {
                if (regularAnimalTypes.contains(animalType)) {
                    for (int aCount = 1; aCount <= 3; aCount++) {
                        ChitCard card = new RegularChitCard(animalType, aCount, board, this);
                        cards.add(card);
                    }
                }
            }

            // generate the PIRATE_DRAGON ChitCard
            for (int i = 1; i <= 2; i++) {
                cards.add(new PirateChitCard(i, board, this));
                cards.add(new PirateChitCard(i, board, this));
            }

            // generate the TRAP chit card
            cards.add(new TrapChitCard(1, board, this));
            cards.add(new TrapChitCard(2, board, this));

            cards.add(new SwapChitCard(board, this));
            cards.add(new SwapChitCard(board, this));


            // shuffle the generated ChitCards
            Collections.shuffle(cards);
        } else if (mode.equals("custom")) {
//            try {
//                // load yaml file into yamlData
//                Yaml yaml = new Yaml();
//                InputStream inputStream = Files.newInputStream(Paths.get("save_file.yaml"));
//                yamlData = yaml.load(inputStream);
//                inputStream.close();
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
            Yaml yaml = new Yaml();
            FileHandle file = Gdx.files.local("save_file.yaml");
            String yamlString = file.readString();

            Map<String, List<String>> yamlData = yaml.load(yamlString);

            // set current player
            activePlayerIndex = Integer.parseInt(String.valueOf(yamlData.get("currentPlayer")));

            // load the data of ChitCard in yaml file
            List<String> chitCardTypeList = yamlData.get("chitCardType");
            List<String> chitCardNumberList = yamlData.get("chitCardNumber");
            List<String> chitCardNameFlipped = yamlData.get("chitCardFlipped");
            for (int i = 0; i < chitCardTypeList.size(); i++) {
                ChitCard newChitCard;
                if (chitCardTypeList.get(i).equals("PIRATE_DRAGON")) {
                    newChitCard = new PirateChitCard(Integer.parseInt(chitCardNumberList.get(i)), board, this);
                } else if (chitCardTypeList.get(i).equals("SWAP")) {
                    newChitCard = new SwapChitCard(board, this);
                } else if (chitCardTypeList.get(i).equals("TRAP")) {
                    newChitCard = new TrapChitCard(Integer.parseInt(chitCardNumberList.get(i)), board, this);
                } else {
                    newChitCard = new RegularChitCard(animalTypeMap.get(chitCardTypeList.get(i)), Integer.parseInt(chitCardNumberList.get(i)), board, this);
                }
                newChitCard.flipped = Boolean.parseBoolean(chitCardNameFlipped.get(i));
                cards.add(newChitCard);
            }

        }


        chitCards = new ChitCard[16];
        chitCards = cards.toArray(chitCards);
    }

    // Perform operations to end the turn by resetting all chit cards
    public void endTurn() {
        // the players whose turn are going to get skip following this turn
        activePlayerIndex = (activePlayerIndex + 1) % players.length;
        Player nextPlayer = players[activePlayerIndex];
        int nextPlayerTrappedTurn = playerTrappedTurnRemaining.get(nextPlayer);
        while (nextPlayerTrappedTurn > 0) {
            playerTrappedTurnRemaining.put(nextPlayer, nextPlayerTrappedTurn - 1);

            activePlayerIndex = (activePlayerIndex + 1) % players.length;
            nextPlayer = players[activePlayerIndex];
            nextPlayerTrappedTurn = playerTrappedTurnRemaining.get(nextPlayer);
        }

        for (ChitCard chitCard : chitCards) {
            chitCard.resetFlipState();
        }
    }

    // see ITurnManager
    public Player getActivePlayer() {
        return this.players[activePlayerIndex];
    }

    // return all player that are currently in the game
    @Override
    public Player[] getAllPlayers() {
        return this.players;
    }

    // get the array of every chit card in the game.
    public ChitCard[] getChitCards() { return this.chitCards; }

    @Override
    public Map<String, List<String>> saveChitCard() {
        HashMap<String, List<String>> chitCardData = new HashMap<>();
        List<String> chitCardType = new ArrayList<>();
        List<String> chitCardNumber = new ArrayList<>();
        List<String> chitCardFlipped = new ArrayList<>();

        for (ChitCard chitCard: chitCards) {
            chitCardType.add(chitCard.getType().toString());
            chitCardNumber.add(String.valueOf(chitCard.getAnimalCount()));
            chitCardFlipped.add(String.valueOf(chitCard.flipped));
        }

        chitCardData.put("chitCardType", chitCardType);
        chitCardData.put("chitCardNumber", chitCardNumber);
        chitCardData.put("chitCardFlipped", chitCardFlipped);
        return chitCardData;
    }

    @Override
    public Map<String, Integer> saveCurrentPlayer() {
        HashMap<String, Integer> playerData = new HashMap<>();
        playerData.put("currentPlayer", this.activePlayerIndex);
        return playerData;
    }

    // skip a number of the following turns for a player
    // @params
    //  numberOfTurn (int) : the number of turns to skip
    //  player (Player) : the player whose turn we're skipping
    @Override
    public void trapPlayerTurn(int numberOfTurn, Player player) {
        if (numberOfTurn > 0) {
            playerTrappedTurnRemaining.put(player, numberOfTurn);
        }
    }

    // Return a map that contain the player whose turn are getting skipped
    // and how many turn they're getting skipped for
    public Map<Player, Integer> getTrappedPlayer() {
        // cloning the map and returning it
        Map<Player, Integer> returnMap = new LinkedHashMap<>();
        for (Player p: playerTrappedTurnRemaining.keySet()) {
            returnMap.put(p, playerTrappedTurnRemaining.get(p));
        }
        return returnMap;
    }

}
