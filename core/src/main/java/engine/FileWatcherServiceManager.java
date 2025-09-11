package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import dungeon.eater.events.core.Event;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.managers.API;
import engine.sui.FileWatcherService;


public class FileWatcherServiceManager implements Disposable {

    private FileWatcherService watcher;

    public void inject (FileWatcherService fileWatcherService) {
        watcher = fileWatcherService;
        watcher.start("ui", "test-ui.xml", () -> {
            Gdx.app.postRunnable(() -> {
                API.get(EventModule.class).fireEvent(UIReloadEvent.class);
            });
        });
    }

    public static class UIReloadEvent extends Event {

    }

    @Override
    public void dispose () {
        if (watcher != null) {
            watcher.dispose();
        }
    }
}
