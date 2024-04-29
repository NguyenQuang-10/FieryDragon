package com.mygdx.game.Board;

// class use to encapsulate data relating to a player
public class Player {
    // name of the player
    protected String name;
    // position on the board
    protected int position = 0;
    // is player still residing in their cave
    public boolean isInCave = true;

    // Constructor
    public Player(String name) {
        this.name = name;
    }

    // get the name of the player
    public String getName(){
        return name;
    }

    // get the position of the player
    public int getPosition(){
        return position;
    }
}
