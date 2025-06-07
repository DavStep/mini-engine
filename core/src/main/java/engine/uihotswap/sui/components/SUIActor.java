package engine.uihotswap.sui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.bootcamp.demo.managers.API;
import engine.uihotswap.sui.SUIManager;
import lombok.Getter;

public abstract class SUIActor<T extends Actor> {

    @Getter
    protected Actor view;

    public SUIActor () {
        initView();
    }

    public void initFromXml (XmlReader.Element xml) {
        initAttributes(xml.getAttributes());
    }

    protected void initAttributes (ObjectMap<String, String> attributes) {
        API.get(SUIManager.class).getActorAttributesApplier().apply(this, attributes);
    }

    protected void initView () {
        view = new Actor();
    }
}
