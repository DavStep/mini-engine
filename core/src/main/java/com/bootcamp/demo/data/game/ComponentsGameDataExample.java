package com.bootcamp.demo.data.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import engine.data.game.IComponentGameData;
import lombok.Getter;

import java.awt.*;

public class ComponentsGameDataExample implements IComponentGameData {
    private final ObjectMap<String, ComponentGameDataExample> components = new ObjectMap<>();

    @Override
    public void load (XmlReader.Element rootXml) {
        final Array<XmlReader.Element> componentGameDataExamplesXML = rootXml.getChildrenByName("example");
        for (XmlReader.Element componentGameDataExampleXML : componentGameDataExamplesXML) {
            final ComponentGameDataExample componentGameDataExample = new ComponentGameDataExample();
            componentGameDataExample.load(componentGameDataExampleXML);
            components.put(componentGameDataExampleXML.getName(), componentGameDataExample);
        }
    }
}
