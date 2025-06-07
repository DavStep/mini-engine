package engine.sui.appliers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import engine.sui.SUIAttributeApplier;
import engine.sui.SUIReaderUtils;
import engine.sui.components.SUIActor;

public final class SUIActorAttributesApplier extends ASUIAttributesApplier<SUIActor> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<SUIActor, String>> attributeAppliers) {
        // scale readers
        attributeAppliers.put("scale", (target, value) -> {
            final Actor view = target.getView();
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                view.setScale(values[0]);
            } else if (values.length == 2) {
                view.setScale(values[0], values[1]);
            } else {
                Gdx.app.error("SUIActorAttributesApplier", "scale requires 1 or 2 values, got: " + values.length);
            }
        });

        // size readers
        attributeAppliers.put("width", (target, value) -> {
            final Actor view = target.getView();
            view.setWidth(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("height", (target, value) -> {
            final Actor view = target.getView();
            view.setHeight(SUIReaderUtils.parseFloat(value));
        });
        attributeAppliers.put("size", (target, value) -> {
            final Actor view = target.getView();
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                view.setSize(values[0], values[0]);
            } else if (values.length == 2) {
                view.setSize(values[0], values[1]);
            } else {
                Gdx.app.error("SUIActorAttributesApplier", "size requires 1 or 2 values, got: " + values.length);
            }
        });

        // rotation reader
        attributeAppliers.put("rotation", (target, value) -> {
            final Actor view = target.getView();
            view.setRotation(SUIReaderUtils.parseFloat(value));
        });

        // position reader
        attributeAppliers.put("position", (target, value) -> {
            final Actor view = target.getView();
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                view.setPosition(values[0], values[0]);
            } else if (values.length == 2) {
                view.setPosition(values[0], values[1]);
            } else {
                Gdx.app.error("SUIActorAttributesApplier", "position requires 1 or 2 values, got: " + values.length);
            }
        });
    }
}
