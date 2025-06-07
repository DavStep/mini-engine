package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.bootcamp.demo.events.core.Event;
import com.bootcamp.demo.events.core.EventModule;
import com.bootcamp.demo.managers.API;
import engine.uihotswap.FileWatcherService;


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
