package engine.data.save;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.bootcamp.demo.data.save.ISaveData;
import lombok.Getter;
import lombok.Setter;

public class CardSaveData implements ISaveData {

    @Getter @Setter
    private String name;
    @Getter @Setter
    private int level;

    @Override
    public void write (Json json) {
        json.writeValue("n", name);
        json.writeValue("l", level);
    }

    @Override
    public void read (Json json, JsonValue jsonValue) {
        name = jsonValue.getString("n");
        level = jsonValue.getInt("l");
    }
}
