package engine.sui.appliers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import engine.Resources;
import engine.sui.SUIAttributeApplier;
import engine.sui.SUIReaderUtils;
import engine.sui.SUISquircle;
import engine.sui.components.SUITable;

public final class SUITableAttributesApplier extends ASUIAttributesApplier<SUITable> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<SUITable, String>> attributeAppliers) {
        attributeAppliers.put("background", (target, value) -> {
            target.getView().setBackground(Resources.getDrawable(value));
        });

        // pad attributes
        attributeAppliers.put("pad", (target, value) -> {
            final Table view = target.getView();
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                view.pad(values[0]);
            } else if (values.length == 2) {
                view.pad(values[0], values[1], values[0], values[1]);
            } else if (values.length == 4) {
                view.pad(values[0], values[1], values[2], values[3]);
            } else {
                Gdx.app.error("SUITableAttributeApplier", "pad requires 1,2 or 4 values, got: " + values.length);
            }
        });
        attributeAppliers.put("padTop", (target, value) -> {
            final Table view = target.getView();
            view.padTop(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("padLeft", (target, value) -> {
            final Table view = target.getView();
            view.padLeft(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("padBottom", (target, value) -> {
            final Table view = target.getView();
            view.padBottom(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("padRight", (target, value) -> {
            final Table view = target.getView();
            view.padRight(SUIReaderUtils.parseFloat(value));
        });

        attributeAppliers.put("color", (target, value) -> {
            final int[] cornerRadius = target.getCornerRadius();
            final Color backgroundColor = Color.valueOf(value);
            target.setBackgroundColor(backgroundColor);

            // update background
            final Table view = target.getView();
            view.setBackground(SUISquircle.get(cornerRadius, backgroundColor));
        });
        attributeAppliers.put("radius", (target, value) -> {
            final int[] cornerRadius = SUIReaderUtils.parseInts(value);
            System.arraycopy(cornerRadius, 0, target.getCornerRadius(), 0, cornerRadius.length);
            final Color backgroundColor = target.getBackgroundColor();

            // update background
            final Table view = target.getView();
            view.setBackground(SUISquircle.get(cornerRadius, backgroundColor));
        });

        // TODO: 15.06.25 rethink the design
        attributeAppliers.put("borderColor", (target, value) -> {
            final int[] cornerRadius = target.getCornerRadius();
            final Color backgroundColor = target.getBackgroundColor();
            backgroundColor.set(Color.valueOf(value));

            final Image border = new Image(SUISquircle.getBorder(cornerRadius, backgroundColor));
            border.setFillParent(true);

            // update background
            final Table view = target.getView();
            view.addActor(border);
        });
    }
}
