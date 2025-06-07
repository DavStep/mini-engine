package engine.uihotswap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SUICellReader {

    @FunctionalInterface
    public interface Reader {
        void apply (Cell<?> cell, String value);
    }

    public static final ObjectMap<String, Reader> readers = new ObjectMap<>();

    static {
        // boolean attributes
        SUIUtils.addBoolean(readers, "expand", Cell::expand);
        SUIUtils.addBoolean(readers, "expandX", Cell::expandX);
        SUIUtils.addBoolean(readers, "expandY", Cell::expandY);
        SUIUtils.addBoolean(readers, "fill", Cell::fill);
        SUIUtils.addBoolean(readers, "fillX", Cell::fillX);
        SUIUtils.addBoolean(readers, "fillY", Cell::fillY);
        SUIUtils.addBoolean(readers, "uniform", Cell::uniform);
        SUIUtils.addBoolean(readers, "uniformX", Cell::uniformX);
        SUIUtils.addBoolean(readers, "uniformY", Cell::uniformY);

        // single float attributes
        SUIUtils.addFloat(readers, "width", Cell::width);
        SUIUtils.addFloat(readers, "height", Cell::height);

        // size and pad readers
        readers.put("size", SUIUtils.fixedFloatArrayReader("size", 2, (cell, vals) -> cell.size(vals[0], vals[1])));
        readers.put("pad", SUIUtils.padReader());

        // align reader
        readers.put("align", SUIUtils.alignReader());
    }
}

// utility class for common SUI readers
class SUIUtils {
    public static final ObjectIntMap<String> alignLookup = new ObjectIntMap<>();

    static {
        // populate align lookup
        alignLookup.put("center", com.badlogic.gdx.utils.Align.center);
        alignLookup.put("top", com.badlogic.gdx.utils.Align.top);
        alignLookup.put("bottom", com.badlogic.gdx.utils.Align.bottom);
        alignLookup.put("left", com.badlogic.gdx.utils.Align.left);
        alignLookup.put("right", com.badlogic.gdx.utils.Align.right);
        alignLookup.put("topLeft", com.badlogic.gdx.utils.Align.topLeft);
        alignLookup.put("topRight", com.badlogic.gdx.utils.Align.topRight);
        alignLookup.put("bottomLeft", com.badlogic.gdx.utils.Align.bottomLeft);
        alignLookup.put("bottomRight", com.badlogic.gdx.utils.Align.bottomRight);
    }

    // add boolean reader
    public static void addBoolean (ObjectMap<String, SUICellReader.Reader> map, String name, Consumer<Cell<?>> setter) {
        map.put(name, (cell, value) -> {
            if (Boolean.parseBoolean(value.trim())) {
                setter.accept(cell);
            }
        });
    }

    // add single-float reader
    public static void addFloat (ObjectMap<String, SUICellReader.Reader> map, String name, BiConsumer<Cell<?>, Float> setter) {
        map.put(name, (cell, value) -> {
            try {
                setter.accept(cell, Float.parseFloat(value.trim()));
            } catch (NumberFormatException e) {
                Gdx.app.error("SUICellReader", "Invalid " + name + " attribute: " + value, e);
            }
        });
    }

    // fixed-length float-array reader
    public static SUICellReader.Reader fixedFloatArrayReader (String name, int length, BiConsumer<Cell<?>, float[]> consumer) {
        return (cell, value) -> {
            final float[] vals = parseFloats(value);
            if (vals.length == length) {
                consumer.accept(cell, vals);
            } else {
                Gdx.app.error("SUICellReader", name + " requires " + length + " values, got: " + vals.length);
            }
        };
    }

    // pad reader
    public static SUICellReader.Reader padReader () {
        return (cell, value) -> {
            float[] v = parseFloats(value);
            switch (v.length) {
                case 1:
                    cell.pad(v[0]);
                    break;
                case 2:
                    cell.pad(v[0], v[1], v[0], v[1]);
                    break;
                case 4:
                    cell.pad(v[0], v[1], v[2], v[3]);
                    break;
                default:
                    Gdx.app.error("SUICellReader", "pad requires 1,2, or 4 values, got: " + v.length);
            }
        };
    }

    // align reader
    public static SUICellReader.Reader alignReader () {
        return (cell, value) -> {
            final String key = value.trim();
            if (alignLookup.containsKey(key)) {
                cell.align(alignLookup.get(key, Align.center));
            } else {
                Gdx.app.error("SUICellReader", "Unknown align value: " + value);
            }
        };
    }

    // parse comma-separated floats
    public static float[] parseFloats (String value) {
        final String trimmed = value.trim();
        if (trimmed.isEmpty()) return new float[0];
        final String[] parts = trimmed.split("\\s*,\\s*");
        float[] r = new float[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) r[i] = Float.parseFloat(parts[i]);
        } catch (NumberFormatException e) {
            Gdx.app.error("SUICellReader", "Invalid floats: " + value, e);
            return new float[0];
        }
        return r;
    }
}
