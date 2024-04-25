package com.mygdx.game.Board;

public class Player {
    protected String name;
    protected int position = 0;
    public boolean isInCave = true;

    public Player(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getPosition(){
        return position;
    }
}
