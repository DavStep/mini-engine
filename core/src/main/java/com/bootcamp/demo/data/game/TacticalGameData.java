package com.bootcamp.demo.data.game;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.XmlReader;
import engine.Resources;
import lombok.Getter;

public class TacticalGameData implements IGameData {
    @Getter
    private String name;
    @Getter
    private Drawable icon;

    @Override
    public void load (XmlReader.Element rootXml) {
        name = rootXml.getAttribute("name");
        icon = Resources.getDrawable(rootXml.getAttribute("icon"));
    }
}
