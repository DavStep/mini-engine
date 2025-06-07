package engine.uihotswap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.Locale;

public enum XmlKey {
    TABLE,
    IMAGE,
    ROW,
    DEFAULTS,
    ;

    private static final ObjectMap<String, XmlKey> RAW_KEYS_MAP = new ObjectMap<>();
    private static final Array<XmlKey> KEYS = new Array<>();
    static {
        final XmlKey[] values = values();
        for (XmlKey key : values) {
            RAW_KEYS_MAP.put(key.name().toLowerCase(Locale.ENGLISH), key);
            KEYS.add(key);
        }
    }

    private static final ObjectSet<XmlKey> ACTORS = new ObjectSet<>();
    static {
        ACTORS.add(XmlKey.TABLE);
        ACTORS.add(XmlKey.IMAGE);
        ACTORS.add(XmlKey.ROW);
    }

    private static final ObjectMap<XmlKey, Class<? extends Actor>> OBJECTS = new ObjectMap<>();
    static {
        OBJECTS.put(XmlKey.TABLE, Table.class);
        OBJECTS.put(XmlKey.IMAGE, Image.class);
    }

    public static XmlKey getKey (String rawKey) {
        final String upperRawKey = rawKey.toLowerCase(Locale.ENGLISH);
        return KEYS.first();
    }
}
