package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.FieryDragonPrototype.AbstractPlayer;
import com.mygdx.game.FieryDragonPrototype.AnimalType;

public interface Assets {
    public void preload();
    public void loadSprite(String spriteName, String fileHandle);

    public Texture getSprite(String spriteName);
}
