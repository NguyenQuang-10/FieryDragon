package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.FieryDragonPrototype.AnimalType;
import java.util.HashMap;
import java.util.Map;

// create an abstract class from this
public class FieryDragonAssets  implements Assets{
    final private Map<String, Texture> spriteNameMap = new HashMap<String, Texture>();

    private static Assets instance;

    private FieryDragonAssets(){}
    public static Assets getInstance(){
        if (instance == null) {
            instance = new FieryDragonAssets();
        }
        return instance;
    }

    public void preload() {
        loadAnimalAssets();
    }
    public void loadSprite(String spriteName, String fileHandle) {
        if (spriteName != null || fileHandle != null) {
            Texture texture = new Texture(Gdx.files.internal(fileHandle));
            spriteNameMap.put(spriteName, texture);
        }
    }

    @Override
    public Texture getSprite(String spriteName) {
        return spriteNameMap.get(spriteName);
    }

    private void loadAnimalAssets(){
        AnimalType[] types = AnimalType.values();
        for (int i = 0; i < types.length; i++){
            String caveName = types[i].caveSpriteName;
            String volcanoName = types[i].volcanoSpriteName;
            if (caveName != null ){
                loadSprite(caveName, String.format("Caves\\%s.png", caveName));
            }
            if (volcanoName != null) {
                loadSprite(volcanoName, String.format("Volcanoes\\%s.png", volcanoName));
            }
        }
    }


    public void dispose() {

    }

}
