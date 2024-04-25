package com.mygdx.game.ChitCards;

import com.mygdx.game.Board.Board;
import com.mygdx.game.Board.Player;

public abstract class ChitCardManager {
    protected Player[] players;
    protected ChitCard[] chitCards;
    int activePlayerIndex; // index of players whose turn belong to

    public ChitCardManager(Player[] players){
        this.players = players;
        this.activePlayerIndex = 0;
    }

    public abstract void generateChitCards(Board board);

    public void endTurn() {
        this.activePlayerIndex = (activePlayerIndex + 1) % this.players.length;
        for (ChitCard chitCard : chitCards) {
            chitCard.resetFlipState();
        }
    }

    public Player getActivePlayer() {
        return this.players[activePlayerIndex];
    }
    public ChitCard[] getChitCards() { return this.chitCards; }
}
