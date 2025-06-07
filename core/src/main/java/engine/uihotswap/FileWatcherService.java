package engine.uihotswap;

public interface FileWatcherService {
    void start (String folderPath, String targetFileName, Runnable onChange);
    void dispose ();
}
