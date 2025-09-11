package com.bootcamp.demo.lwjgl3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import dungeon.eater.DungeonEater;
import engine.sui.FileWatcherService;

import java.io.IOException;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class DesktopLauncher extends Lwjgl3Application {

    public static void main (String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.

        final Lwjgl3ApplicationConfiguration defaultConfiguration = getDefaultConfiguration();
        final DungeonEater demoGame = new DungeonEater();
        demoGame.setFileWatcherService(new FileWatcherService() {
            private FileWatcher watcher;

            @Override
            public void start (String folderPath, String fileName, Runnable onChange) {
                try {
                    watcher = new FileWatcher(folderPath, fileName, path -> {
                        Gdx.app.postRunnable(onChange);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void dispose () {
                if (watcher != null) watcher.dispose();
            }
        });
        new DesktopLauncher(demoGame, defaultConfiguration);
    }

    public DesktopLauncher (ApplicationListener listener, Lwjgl3ApplicationConfiguration config) {
        super(listener, config);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration () {
        float[] res = new float[]{2560, 1440}; // default

        float scale = 0.5f;

        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode((int) (res[0] * scale), (int) (res[1] * scale));
        config.setForegroundFPS(60);
        config.setHdpiMode(HdpiMode.Pixels);
        config.setWindowPosition(200, 200);
        config.setTitle("mini zoqanch");
        return config;
    }
}
