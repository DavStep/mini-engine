package dungeon.eater.data.save;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import engine.data.save.IComponentSaveData;
import lombok.Getter;
import lombok.Setter;

public class ComponentSaveDataExample implements IComponentSaveData {

    @Getter @Setter
    private String name;

    @Override
    public void write (Json json) {
        json.writeValue("n", name);
    }

    @Override
    public void read (Json json, JsonValue jsonValue) {
        name = jsonValue.getString("n");
    }
}
