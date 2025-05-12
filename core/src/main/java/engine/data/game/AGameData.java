package engine.data.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

public abstract class AGameData {

    private final XmlReader xmlReader = new XmlReader();

    public void load () {

    }

    protected XmlReader.Element readXml (String internalPath) {
        return xmlReader.parse(Gdx.files.internal(internalPath + ".xml"));
    }
}
