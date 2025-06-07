package com.bootcamp.demo.lwjgl3;

import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

public class FileWatcher {

    private final WatchService watchService;
    private final Thread watchThread;

    public FileWatcher (final String folderPath, final String targetFileName, final Consumer<Path> onChange) throws IOException {
        final Path dir = Paths.get(folderPath);
        this.watchService = FileSystems.getDefault().newWatchService();
        dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        this.watchThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final WatchKey key = watchService.take();
                    for (final WatchEvent<?> event : key.pollEvents()) {
                        final Path changed = (Path) event.context();
                        if (changed.getFileName().toString().equals(targetFileName)) {
                            onChange.accept(dir.resolve(changed));
                        }
                    }
                    key.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }, "FileWatcherThread");
        this.watchThread.setDaemon(true);
        this.watchThread.start();
    }

    public void dispose () {
        try {
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        watchThread.interrupt();
    }
}
