package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;
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
                if (animalType != AnimalType.PIRATE_DRAGON) {
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

            // load the data of ChitCard in yaml file
            List<String> chitCardTypeList = yamlData.get("chitCardType");
            List<String> chitCardNumberList = yamlData.get("chitCardNumber");
            for (int i = 0; i < chitCardTypeList.size(); i++) {
                if (chitCardTypeList.get(i).equals("PIRATE_DRAGON")) {
                    cards.add(new PirateChitCard(Integer.parseInt(chitCardNumberList.get(i)), board, this));
                } else {
                    cards.add(new RegularChitCard(animalTypeMap.get(chitCardTypeList.get(i)), Integer.parseInt(chitCardNumberList.get(i)), board, this));
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

    // get the array of every chit card in the game.
    public ChitCard[] getChitCards() { return this.chitCards; }
}
