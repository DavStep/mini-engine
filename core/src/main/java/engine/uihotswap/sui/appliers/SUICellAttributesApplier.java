package engine.uihotswap.sui.appliers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import engine.uihotswap.sui.SUIAttributeApplier;
import engine.uihotswap.sui.SUIReaderUtils;
import engine.uihotswap.sui.components.SUICell;


public class SUICellAttributesApplier extends ASUIAttributesApplier<SUICell> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<SUICell, String>> attributeAppliers) {
        attributeAppliers.put("grow", (target, value) -> {
            final Cell<?> cell = target.getCell();
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
        attributeAppliers.put("expand", (target, value) -> {
            final Cell<?> cell = target.getCell();
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
        attributeAppliers.put("fill", (target, value) -> {
            final Cell<?> cell = target.getCell();
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
        attributeAppliers.put("uniform", (target, value) -> {
            final Cell<?> cell = target.getCell();
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
        attributeAppliers.put("size", (target, value) -> {
            final Cell<?> cell = target.getCell();
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                cell.size(values[0]);
            } else if (values.length == 2) {
                cell.size(values[0], values[1]);
            } else {
                Gdx.app.error("SUICellAttributeApplier", "size requires 1 or 2 values, got: " + values.length);
            }
        });
        attributeAppliers.put("width", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.width(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("height", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.height(SUIReaderUtils.parseFloat(value));
        });

        // pad appliers
        attributeAppliers.put("pad", (target, value) -> {
            final Cell<?> cell = target.getCell();
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
        attributeAppliers.put("padTop", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.padTop(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("padLeft", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.padLeft(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("padBottom", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.padBottom(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("padRight", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.padRight(SUIReaderUtils.parseFloat(value));
        });

        // space appliers
        attributeAppliers.put("space", (target, value) -> {
            final Cell<?> cell = target.getCell();
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                cell.space(values[0]);
            } else if (values.length == 2) {
                cell.space(values[0], values[1], values[0], values[1]);
            } else if (values.length == 4) {
                cell.space(values[0], values[1], values[2], values[3]);
            } else {
                Gdx.app.error("SUICellAttributeApplier", "pad requires 1,2 or 4 values, got: " + values.length);
            }
        });
        attributeAppliers.put("spaceTop", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.spaceTop(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("spaceLeft", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.spaceLeft(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("spaceBottom", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.spaceBottom(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("spaceRight", (target, value) -> {
            final Cell<?> cell = target.getCell();
            cell.spaceRight(SUIReaderUtils.parseFloat(value));
        });

        // align appliers
        attributeAppliers.put("align", (target, value) -> {
            final Cell<?> cell = target.getCell();
            final String key = value.trim();
            if (SUIReaderUtils.alignLookup.containsKey(key)) {
                cell.align(SUIReaderUtils.alignLookup.get(key, Align.center));
            } else {
                Gdx.app.error("SUICellAttributeApplier", "Unknown align value: " + value);
            }
        });
    }
}
