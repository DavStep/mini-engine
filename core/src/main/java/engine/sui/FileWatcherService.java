package engine.sui;

public interface FileWatcherService {
    void start (String folderPath, String targetFileName, Runnable onChange);
    void dispose ();
}
