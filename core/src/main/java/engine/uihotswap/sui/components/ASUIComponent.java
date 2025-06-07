package engine.uihotswap.sui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader;

public abstract class ASUIComponent<T extends Actor> {

    public void initFromXml (XmlReader.Element xml) {
        initAttributes(xml);
    }

    protected abstract void initAttributes (XmlReader.Element xml);

    public abstract T getActor ();
}
