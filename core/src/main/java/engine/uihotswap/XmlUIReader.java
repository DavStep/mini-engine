package engine.uihotswap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.bootcamp.demo.events.core.EventHandler;
import com.bootcamp.demo.events.core.EventListener;
import com.bootcamp.demo.events.core.EventModule;
import com.bootcamp.demo.managers.API;
import engine.FileWatcherServiceManager;
import engine.Resources;
import engine.Squircle;
import lombok.Getter;

public class XmlUIReader implements EventListener {

    @Getter
    private final Table rootTable = new Table();
    private final XmlReader reader = new XmlReader();
    private final String path = "ui/test-ui.xml";



    public XmlUIReader () {
        API.get(EventModule.class).registerListener(this);
        reloadUI();
    }

    @EventHandler
    public void onUIReloaded (FileWatcherServiceManager.UIReloadEvent event) {
        reloadUI();
    }

    private void reloadUI () {
        rootTable.clearChildren();

        final XmlReader.Element rootXml = reader.parse(Gdx.files.internal(path));
        final XmlUIData component = new XmlUIData(rootXml);

        // now build recursively
        buildTable(component, rootTable);
    }

    private void buildTable (final XmlUIData component, final Table parentTable) {
//        final String name = component.getName();
//
//        if (name.equals(XmlKey.TABLE)) {
//            try {
//                final Table table = instantiateObject(XmlKey.TABLE);
//                TableAttributeParser.apply(component, table);
//
//                final Cell<Actor> cell = parentTable.add(table);
//                CellAttributeParser.apply(component, cell);
//
//                for (XmlUIData child : component.getChildren()) {
//                    buildTable(child, table);
//                }
//
//                table.debugAll();
//            } catch (Exception e) {
//                System.out.println("shit went bad");
//            }
//        }
//
//        if (name.equals(XmlKey.IMAGE)) {
//            parentTable.row();
//        }
//
//        if (name.equals(XmlKey.DEFAULTS)) {
//            CellAttributeParser.apply(component, parentTable.defaults());
//        }
    }

    public static <T extends Actor> T instantiateObject (XmlKey key) {
//        try {
//            return (T) ClassReflection.newInstance(XmlKey.OBJECTS.get(key));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

    public static class CellAttributeParser {
        private static final ObjectMap<String, Integer> ALIGN_MAP = new ObjectMap<>();

        static {
            ALIGN_MAP.put("center", Align.center);
            ALIGN_MAP.put("top", Align.top);
            ALIGN_MAP.put("bottom", Align.bottom);
            ALIGN_MAP.put("left", Align.left);
            ALIGN_MAP.put("right", Align.right);
            ALIGN_MAP.put("topLeft", Align.topLeft);
            ALIGN_MAP.put("topRight", Align.topRight);
            ALIGN_MAP.put("bottomLeft", Align.bottomLeft);
            ALIGN_MAP.put("bottomRight", Align.bottomRight);
        }

        private static final ObjectMap<String, Reader> readers = new ObjectMap<>();

        static {
            readers.put("size", (cell, value) -> {
                cell.size(Integer.parseInt(value));
            });
            readers.put("pad", (cell, value) -> {
                final String[] split = value.split(",");
                if (split.length == 1) {
                    final float pad = Float.parseFloat(split[0].trim());
                    cell.pad(pad);
                } else if (split.length == 2) {
                    final float vertical = Float.parseFloat(split[0].trim());
                    final float horizontal = Float.parseFloat(split[1].trim());
                    cell.pad(vertical, horizontal, vertical, horizontal);
                } else if (split.length == 4) {
                    final float top = Float.parseFloat(split[0].trim());
                    final float left = Float.parseFloat(split[1].trim());
                    final float bottom = Float.parseFloat(split[2].trim());
                    final float right = Float.parseFloat(split[3].trim());
                    cell.pad(top, left, bottom, right);
                }
            });
            readers.put("space", (cell, value) -> {
                final String[] split = value.split(",");
                if (split.length == 1) {
                    final float space = Float.parseFloat(split[0].trim());
                    cell.space(space);
                } else if (split.length == 2) {
                    final float vertical = Float.parseFloat(split[0].trim());
                    final float horizontal = Float.parseFloat(split[1].trim());
                    cell.space(vertical, horizontal, vertical, horizontal);
                } else if (split.length == 4) {
                    final float top = Float.parseFloat(split[0].trim());
                    final float left = Float.parseFloat(split[1].trim());
                    final float bottom = Float.parseFloat(split[2].trim());
                    final float right = Float.parseFloat(split[3].trim());
                    cell.space(top, left, bottom, right);
                }
            });
            readers.put("fill", (cell, value) -> {
                final String[] split = value.split(",");
                if (split.length == 1) {
                    final boolean fill = Boolean.parseBoolean(split[0].trim());
                    cell.fill(fill);
                } else if (split.length == 2) {
                    final boolean x = Boolean.parseBoolean(split[0].trim());
                    final boolean y = Boolean.parseBoolean(split[1].trim());
                    cell.fill(x, y);
                }
            });
            readers.put("expand", (cell, value) -> {
                final String[] split = value.split(",");
                if (split.length == 1) {
                    final boolean expand = Boolean.parseBoolean(split[0].trim());
                    if (expand) cell.expand();
                } else if (split.length == 2) {
                    final boolean x = Boolean.parseBoolean(split[0].trim());
                    final boolean y = Boolean.parseBoolean(split[1].trim());
                    cell.expand(x, y);
                }
            });
            readers.put("grow", (cell, value) -> {
                final String[] split = value.split(",");
                if (split.length == 1) {
                    final boolean grow = Boolean.parseBoolean(split[0].trim());
                    if (grow) cell.grow();
                } else if (split.length == 2) {
                    final boolean x = Boolean.parseBoolean(split[0].trim());
                    final boolean y = Boolean.parseBoolean(split[1].trim());
                    if (x) cell.growX();
                    if (y) cell.growY();
                }
            });
            readers.put("align", (cell, value) -> {
                final int align = ALIGN_MAP.get(value.trim());
                cell.align(align);
            });
        }

        public interface Reader {
            void process (Cell<? extends Actor> cell, String value);
        }

        private static void apply (final XmlUIData component, final Cell<Actor> cell) {
            for (ObjectMap.Entry<String, Reader> entry : readers) {
                final String attribute = entry.key;
                if (!component.attributes.containsKey(attribute)) continue;
                final ObjectMap<String, String> attributes = component.getAttributes();
                readers.get(attribute).process(cell, attributes.get(attribute));
            }
        }
    }

    public static class TableAttributeParser {
        private static final ObjectMap<String, Reader> readers = new ObjectMap<>();

        static {
            readers.put("background", (table, value, meta) -> {
                table.setBackground(Resources.getDrawable(value));
            });
            readers.put("radius", (table, value, meta) -> {
                final String[] split = value.split(",");

                meta.radius = new Array<>(4);
                if (split.length == 1) {
                    final int radius = Integer.parseInt(split[0].trim());
                    for (int i = 0; i < 4; i++) {
                        meta.radius.add(radius);
                    }
                } else if (split.length == 4) {
                    for (int i = 0; i < 4; i++) {
                        meta.radius.add(Integer.parseInt(split[i].trim()));
                    }
                }

                final Drawable squircle = Squircle.getSquircle(meta.radius.get(0), meta.radius.get(1), meta.radius.get(2), meta.radius.get(3), meta.color);
                table.setBackground(squircle);
            });
            readers.put("color", (table, value, meta) -> {
                final String colorHex = value.trim();
                meta.color = Color.valueOf(colorHex);

                if (meta.backgroundPath != null) {
                    table.setBackground(Resources.getDrawable(meta.backgroundPath, meta.color));
                }
                else if (meta.radius != null) {
                    final Drawable squircle = Squircle.getSquircle(meta.radius.get(0), meta.radius.get(1), meta.radius.get(2), meta.radius.get(3), meta.color);
                    table.setBackground(squircle);
                }
            });
        }

        public interface Reader {
            void process (Table table, String value, Meta meta);
        }

        private static void apply (final XmlUIData component, final Table table) {
            final Meta meta = new Meta();

            for (ObjectMap.Entry<String, Reader> entry : readers) {
                final String attribute = entry.key;
                if (!component.attributes.containsKey(attribute)) continue;
                final ObjectMap<String, String> attributes = component.getAttributes();
                readers.get(attribute).process(table, attributes.get(attribute), meta);
            }
        }

        private static class Meta {
            public Array<Integer> radius;
            public String backgroundPath;
            public Color color = Color.WHITE;
        }
    }
}
