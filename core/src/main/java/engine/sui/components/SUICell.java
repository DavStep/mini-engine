package engine.sui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.bootcamp.demo.managers.API;
import engine.sui.SUIAttributeApplier;
import engine.sui.SUIManager;
import engine.sui.SUIReaderUtils;
import engine.sui.appliers.ASUIAttributesApplier;
import lombok.Getter;
import lombok.Setter;

public class SUICell extends SUIActor<Actor> {

    @Getter @Setter
    private Cell<?> cell;

    @Override
    public void initFromXml (XmlReader.Element xml) {
        if (xml.getChildCount() > 0) {
            initActor(xml.getChild(0));
        }
        super.initFromXml(xml);
    }

    @Override
    protected void initAttributes (ObjectMap<String, String> attributes) {
        API.get(SUIManager.class).getCellAttributesApplier().apply(this, attributes);
    }

    private void initActor (XmlReader.Element xml) {
        final SUIActor<?> childComponent = API.get(SUIManager.class).createElement(xml);
        final Actor childActor = childComponent.getView();
        cell.setActor(childActor);
    }

    @Override
    public Actor getView () {
        return cell.getActor();
    }

    public static class AttributesApplier extends ASUIAttributesApplier<Cell<?>> {

        @Override
        public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<Cell<?>, String>> attributeAppliers) {
            attributeAppliers.put("grow", (cell, value) -> {
                if (value.equals("xy")) {
                    cell.grow();
                    return;
                }
                final String[] values = SUIReaderUtils.parseStrings(value);
                for (String s : values) {
                    if (s.equals("x")) cell.growX();
                    if (s.equals("y")) cell.growY();
                }
            });
            attributeAppliers.put("expand", (cell, value) -> {
                if (value.equals("xy")) {
                    cell.expand();
                    return;
                }
                final String[] values = SUIReaderUtils.parseStrings(value);
                for (String s : values) {
                    if (s.equals("x")) cell.expandX();
                    if (s.equals("y")) cell.expandY();
                }
            });
            attributeAppliers.put("fill", (cell, value) -> {
                if (value.equals("xy")) {
                    cell.fill();
                    return;
                }
                final String[] values = SUIReaderUtils.parseStrings(value);
                for (String s : values) {
                    if (s.equals("x")) cell.fillX();
                    if (s.equals("y")) cell.fillY();
                }
            });
            attributeAppliers.put("uniform", (cell, value) -> {
                if (value.equals("xy")) {
                    cell.uniform();
                    return;
                }
                final String[] values = SUIReaderUtils.parseStrings(value);
                for (String s : values) {
                    if (s.equals("x")) cell.uniformX();
                    if (s.equals("y")) cell.uniformY();
                }
            });

            // size appliers
            SUIReaderUtils.addFloat(attributeAppliers, "width", Cell::width);
            SUIReaderUtils.addFloat(attributeAppliers, "height", Cell::height);
            attributeAppliers.put("size", (cell, value) -> {
                final float[] values = SUIReaderUtils.parseFloats(value);
                if (values.length == 1) {
                    cell.size(values[0]);
                } else if (values.length == 2) {
                    cell.size(values[0], values[1]);
                } else {
                    Gdx.app.error("SUICellAttributeApplier", "size requires 1 or 2 values, got: " + values.length);
                }
            });

            // pad appliers
            attributeAppliers.put("pad", (cell, value) -> {
                final float[] values = SUIReaderUtils.parseFloats(value);
                if (values.length == 1) {
                    cell.pad(values[0]);
                } else if (values.length == 2) {
                    cell.pad(values[0], values[1], values[0], values[1]);
                } else if (values.length == 4) {
                    cell.pad(values[0], values[1], values[2], values[3]);
                } else {
                    Gdx.app.error("SUICellAttributeApplier", "pad requires 1,2 or 4 values, got: " + values.length);
                }
            });
            SUIReaderUtils.addFloat(attributeAppliers, "padLeft", Cell::padLeft);
            SUIReaderUtils.addFloat(attributeAppliers, "padRight", Cell::padRight);
            SUIReaderUtils.addFloat(attributeAppliers, "padTop", Cell::padTop);
            SUIReaderUtils.addFloat(attributeAppliers, "padBottom", Cell::padBottom);

            // space appliers
            attributeAppliers.put("space", (cell, value) -> {
                final float[] values = SUIReaderUtils.parseFloats(value);
                if (values.length == 1) {
                    cell.space(values[0]);
                } else if (values.length == 2) {
                    cell.space(values[0], values[1], values[0], values[1]);
                } else if (values.length == 4) {
                    cell.space(values[0], values[1], values[2], values[3]);
                } else {
                    Gdx.app.error("SUICellAttributeApplier", "space requires 1,2 or 4 values, got: " + values.length);
                }
            });
            SUIReaderUtils.addFloat(attributeAppliers, "spaceLeft", Cell::spaceLeft);
            SUIReaderUtils.addFloat(attributeAppliers, "spaceRight", Cell::spaceRight);
            SUIReaderUtils.addFloat(attributeAppliers, "spaceTop", Cell::spaceTop);
            SUIReaderUtils.addFloat(attributeAppliers, "spaceBottom", Cell::spaceBottom);

            // align appliers
            attributeAppliers.put("align", (cell, value) -> {
                final String key = value.trim();
                if (SUIReaderUtils.alignLookup.containsKey(key)) {
                    cell.align(SUIReaderUtils.alignLookup.get(key, Align.center));
                } else {
                    Gdx.app.error("SUICellAttributeApplier", "Unknown align value: " + value);
                }
            });
        }
    }
}
