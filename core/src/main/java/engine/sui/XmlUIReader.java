package engine.sui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.*;
import com.bootcamp.demo.events.core.EventHandler;
import com.bootcamp.demo.events.core.EventListener;
import com.bootcamp.demo.events.core.EventModule;
import com.bootcamp.demo.managers.API;
import engine.FileWatcherServiceManager;
import engine.sui.components.SUIActor;
import lombok.Getter;

public class XmlUIReader implements EventListener {

    @Getter
    private final Table rootTable = new Table();
    private final XmlReader reader = new XmlReader();
    private final String path = "ui/test-ui.xml";



    public XmlUIReader () {
        API.get(EventModule.class).registerListener(this);
        reloadUI();
    }

    @EventHandler
    public void onUIReloaded (FileWatcherServiceManager.UIReloadEvent event) {
        reloadUI();
    }

    private void reloadUI () {
        rootTable.clearChildren();

        final XmlReader.Element rootXml = reader.parse(Gdx.files.internal(path));
        final SUIActor<?> element = API.get(SUIManager.class).createElement(rootXml);

        // now build recursively
        rootTable.add(element.getView()).grow();

        rootTable.debugAll();
    }
}
