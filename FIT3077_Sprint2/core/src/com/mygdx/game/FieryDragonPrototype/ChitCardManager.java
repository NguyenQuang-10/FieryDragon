package com.mygdx.game.FieryDragonPrototype;

public class ChitCardManager {
    AbstractPlayer[] players;
    ChitCard[] chitCards;
    int activePlayerIndex; // index of players whose turn belong to

    public void setPlayers(AbstractPlayer[] players){
        this.players = players;
        this.activePlayerIndex = 0;
    }

    public void setChitCards(ChitCard[] chitCards) {
        this.chitCards = chitCards;
    }

    public void endTurn() {
        this.activePlayerIndex = (activePlayerIndex + 1) % this.players.length;
        for (int i = 0; i < chitCards.length; i++) {
            chitCards[i].resetFlipState();
        }
    }

    public AbstractPlayer getActivePlayer() {
        return this.players[activePlayerIndex];
    }
}
