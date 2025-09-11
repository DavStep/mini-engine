package dungeon.eater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import dungeon.eater.data.game.GameData;
import dungeon.eater.data.save.SaveData;
import dungeon.eater.events.GameStartedEvent;
import dungeon.eater.managers.API;
import dungeon.eater.events.core.EventModule;
import engine.FileWatcherServiceManager;
import engine.sui.FileWatcherService;
import lombok.Setter;

public class DungeonEater extends com.badlogic.gdx.Game {

    @Setter
    private FileWatcherService fileWatcherService;

    @Override
    public void create () {
        Gdx.input.setInputProcessor(new InputMultiplexer());

        if (fileWatcherService != null) {
            final FileWatcherServiceManager fileWatcherServiceManager = new FileWatcherServiceManager();
            API.Instance().register(FileWatcherServiceManager.class, fileWatcherServiceManager);
            API.get(FileWatcherServiceManager.class).inject(fileWatcherService);
        }

        final GameData gameData = new GameData();
        API.Instance().register(GameData.class, gameData);
        gameData.load();

        loadSaveData();
        setScreen(new ScreenStack());
        API.get(EventModule.class).fireEvent(GameStartedEvent.class);
    }

    private void loadSaveData () {
        final FileHandle file = getPlayerDataFileHandler();
        if (file.exists()) {
            createNewSaveData();
            return;
        }

        final JsonReader jsonReader = new JsonReader();
        final Json json = new Json();
        json.setIgnoreUnknownFields(true);

        final String dataString = file.readString();
        final JsonValue jsonValue = jsonReader.parse(dataString);
        final SaveData saveData = json.readValue(SaveData.class, jsonValue);
        API.Instance().register(SaveData.class, saveData);
    }

    private void createNewSaveData () {
        final SaveData saveData = new SaveData();
        API.Instance().register(SaveData.class, saveData);
        savePlayerData();
    }

    public void savePlayerData () {
        final SaveData saveData = API.get(SaveData.class);

        final Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        final String dataToPersist = json.toJson(saveData);
        getPlayerDataFileHandler().writeString(dataToPersist, false);
    }

    private FileHandle getPlayerDataFileHandler () {
        final FileHandle playerDataFile = Gdx.files.local("usercache").child("player-data");
        // check if file exists; if not, create an empty file
        if (!playerDataFile.exists()) {
            playerDataFile.writeString("", false);
        }
        return playerDataFile;
    }

    @Override
    public void dispose () {
        super.dispose();
        API.Instance().dispose();
        Gdx.app.exit();
    }
}
