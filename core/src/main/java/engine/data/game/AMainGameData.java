package engine.data.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.bootcamp.demo.data.game.TacticalsGameData;
import lombok.Getter;

public abstract class AMainGameData {

    private final XmlReader xmlReader = new XmlReader();

    public AMainGameData () {

    }

    public void load () {

    }

    protected XmlReader.Element readXml (String internalPath) {
        return xmlReader.parse(Gdx.files.internal(internalPath));
    }
}
