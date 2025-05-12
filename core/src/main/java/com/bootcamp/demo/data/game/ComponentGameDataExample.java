package com.bootcamp.demo.data.game;

import com.badlogic.gdx.utils.XmlReader;
import engine.data.game.IComponentGameData;
import lombok.Getter;

public class ComponentGameDataExample implements IComponentGameData {
    @Getter
    private String name;

    @Override
    public void load (XmlReader.Element rootXml) {
        name = rootXml.getAttribute("name");
    }
}
