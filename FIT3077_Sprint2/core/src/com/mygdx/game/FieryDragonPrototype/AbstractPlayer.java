package com.mygdx.game.FieryDragonPrototype;

public class AbstractPlayer {
    protected String name;
    protected int position = 0;
    protected boolean isInCave = true;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public boolean isInCave() {return isInCave;}
    public int getPosition(){
        return position;
    }
}
