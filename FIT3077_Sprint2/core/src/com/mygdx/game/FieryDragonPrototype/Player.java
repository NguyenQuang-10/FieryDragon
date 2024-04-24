package com.mygdx.game.FieryDragonPrototype;

import com.mygdx.game.FieryDragonAssets;

public class Player extends AbstractPlayer {
    public Player(String name, String spriteFile) {
        super(name);
        FieryDragonAssets.getInstance().loadSprite(name, spriteFile);
    }

}
