package dungeon.eater.pages;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import dungeon.eater.pages.core.APage;
import engine.sui.XmlUIReader;

public class TestPage extends APage {

    @Override
    protected void constructContent (Table content) {
        content.add().grow();
    }
}
