package dungeon.eater.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import dungeon.eater.dialogs.core.ADialog;
import dungeon.eater.managers.API;
import engine.sui.SUIManager;
import engine.sui.components.SUIActor;

public class TestDialog extends ADialog {

    private static final String SUI_PATH = "ui/test-dialog.xml";

    @Override
    protected String getTitle () {
        return "Test Dialog";
    }

    @Override
    protected void constructContent (Table content) {
        final XmlReader reader = new XmlReader();
        final XmlReader.Element rootXml = reader.parse(Gdx.files.internal(SUI_PATH));
        final SUIActor<?> root = API.get(SUIManager.class).createElement(rootXml);
        content.add(root.getView()).grow();
    }
}
