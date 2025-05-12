package com.bootcamp.demo.data.game;

import engine.data.game.AGameData;
import lombok.Getter;

public class GameData extends AGameData {

    @Getter
    private final ComponentsGameDataExample gameDataExample;

    public GameData () {
        gameDataExample = new ComponentsGameDataExample();
    }

    @Override
    public void load () {
        gameDataExample.load(readXml("data/game-data-example"));
    }
}
