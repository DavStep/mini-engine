package engine.sui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Consumer;

// utility class for generic SUI readers
public class SUIReaderUtils {

    // align lookup local to this reader
    public static ObjectIntMap<String> alignLookup = new ObjectIntMap<>();

    static {
        alignLookup.put("center", Align.center);
        alignLookup.put("top", Align.top);
        alignLookup.put("bottom", Align.bottom);
        alignLookup.put("left", Align.left);
        alignLookup.put("right", Align.right);
        alignLookup.put("topLeft", Align.topLeft);
        alignLookup.put("topRight", Align.topRight);
        alignLookup.put("bottomLeft", Align.bottomLeft);
        alignLookup.put("bottomRight", Align.bottomRight);
    }

    // add single-float reader for any target type
    public static <T> void addFloat (ObjectMap<String, SUIAttributeApplier<T, String>> map,
                                     String name, SUIAttributeApplier<T, Float> setter) {
        map.put(name, (target, value) -> {
            try {
                setter.apply(target, Float.parseFloat(value.trim()));
            } catch (NumberFormatException e) {
                Gdx.app.error("SUIUtils", "Invalid " + name + " attribute: " + value, e);
            }
        });
    }

    public static <T> void addBoolean (ObjectMap<String, SUIAttributeApplier<T, String>> map,
                                       String name, SUIAttributeApplier<T, Boolean> setter) {
        map.put(name, (target, value) -> {
            try {
                setter.apply(target, Boolean.parseBoolean(value.trim()));
            } catch (NumberFormatException e) {
                Gdx.app.error("SUIUtils", "Invalid " + name + " attribute: " + value, e);
            }
        });
    }

    public static <T> void addBoolean (ObjectMap<String, SUIAttributeApplier<T, String>> map,
                                       String name, Consumer<T> setter) {
        map.put(name, (target, value) -> {
            try {
                setter.accept(target);
            } catch (NumberFormatException e) {
                Gdx.app.error("SUIUtils", "Invalid " + name + " attribute: " + value, e);
            }
        });
    }

    // parse comma-separated floats
    public static float[] parseFloats (final String value) {
        final String[] parts = parseStrings(value);
        final float[] result = new float[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                result[i] = Float.parseFloat(parts[i]);
            }
        } catch (NumberFormatException e) {
            Gdx.app.error("SUIUtils", "Invalid floats: " + value, e);
            return new float[0];
        }
        return result;
    }

    public static float parseFloat (final String value) {
        return Float.parseFloat(value.trim());
    }

    public static int[] parseInts (final String value) {
        final String[] parts = parseStrings(value);
        final int[] result = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                result[i] = Integer.parseInt(parts[i]);
            }
        } catch (NumberFormatException e) {
            Gdx.app.error("SUIUtils", "Invalid floats: " + value, e);
            return new int[0];
        }
        return result;
    }

    // parse comma-separated booleans
    public static boolean[] parseBooleans (final String value) {
        final String[] parts = parseStrings(value);
        final boolean[] result = new boolean[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                result[i] = Boolean.parseBoolean(parts[i]);
            }
        } catch (NumberFormatException e) {
            Gdx.app.error("SUIUtils", "Invalid boolean: " + value, e);
            return new boolean[0];
        }
        return result;
    }

    public static String[] parseStrings (final String value) {
        final String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            Gdx.app.error("SUIUtils", "Invalid string: provided empty value");
            return new String[0];
        }
        final String[] parts = trimmed.split("\\s*,\\s*");
        return parts;
    }
}
