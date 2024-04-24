package com.mygdx.game.FieryDragonPrototype;

public enum AnimalType {
    PIRATE_DRAGON("Pirate Dragon", null, null, "pirateChit"),
    SALAMANDER("Salamander", "salaCave", "salaVol", "salaChit"),
    SPIDER("Spider", "spiderCave", "spiderVol", "spiderChit"),
    BAT("Bat", "batCave", "batVol", "batChit"),
    BABY_DRAGON("Baby Dragon", "dragonCave", "dragonVol", "dragonChit");


    public final String name;
    public final String caveSpriteName;
    public final String volcanoSpriteName;
    public final String chitSpriteName;

    private AnimalType(String name, String caveSpriteName, String volcanoSpriteName, String chitSpriteName) {
        this.name = name;
        this.caveSpriteName = caveSpriteName;
        this.volcanoSpriteName = volcanoSpriteName;
        this.chitSpriteName = chitSpriteName;
    }
}
