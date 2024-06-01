package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;
import com.mygdx.game.ChitCards.Cards.PirateChitCard;
import com.mygdx.game.ChitCards.Cards.RegularChitCard;
import com.mygdx.game.ChitCards.Cards.SwapChitCard;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    // Constructor
    public ChitCardManager(Player[] players, String mode, Map<String, AnimalType> animalTypeMap){
        this.players = players;
        this.activePlayerIndex = 0;
        this.mode = mode;
        this.animalTypeMap = animalTypeMap;
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

            // generate the ChitCards sequentially and add regular ChitCard to the board
            for (AnimalType animalType : animalTypes) {
                if (animalType != AnimalType.PIRATE_DRAGON && animalType != AnimalType.SWAP) {
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

            cards.add(new SwapChitCard(board, this));
            cards.add(new SwapChitCard(board, this));


            // shuffle the generated ChitCards
            Collections.shuffle(cards);
        } else if (mode.equals("custom")) {
            try {
                // load yaml file into yamlData
                Yaml yaml = new Yaml();
                InputStream inputStream = Files.newInputStream(Paths.get("save_file.yaml"));
                yamlData = yaml.load(inputStream);
                inputStream.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // set current player
            activePlayerIndex = Integer.parseInt(String.valueOf(yamlData.get("currentPlayer")));

            // load the data of ChitCard in yaml file
            List<String> chitCardTypeList = yamlData.get("chitCardType");
            List<String> chitCardNumberList = yamlData.get("chitCardNumber");
            List<String> chitCardNameFlipped = yamlData.get("chitCardFlipped");
            for (int i = 0; i < chitCardTypeList.size(); i++) {
                if (chitCardTypeList.get(i).equals("PIRATE_DRAGON")) {
                    PirateChitCard newChitCard = new PirateChitCard(Integer.parseInt(chitCardNumberList.get(i)), board, this);
                    newChitCard.flipped = Boolean.parseBoolean(chitCardNameFlipped.get(i));;
                    cards.add(newChitCard);
                } else if (chitCardTypeList.get(i).equals("SWAP")) {
                    SwapChitCard newChitCard =new SwapChitCard(board, this);
                    newChitCard.flipped = Boolean.parseBoolean(chitCardNameFlipped.get(i));;
                    cards.add(newChitCard);
                } else {
                    RegularChitCard newChitCard = new RegularChitCard(animalTypeMap.get(chitCardTypeList.get(i)), Integer.parseInt(chitCardNumberList.get(i)), board, this);
                    newChitCard.flipped = Boolean.parseBoolean(chitCardNameFlipped.get(i));;
                    cards.add(newChitCard);
                }
            }

        }


        chitCards = new ChitCard[16];
        chitCards = cards.toArray(chitCards);
    }

    // Perform operations to end the turn by resetting all chit cards
    public void endTurn() {
        this.activePlayerIndex = (activePlayerIndex + 1) % this.players.length;
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
}
