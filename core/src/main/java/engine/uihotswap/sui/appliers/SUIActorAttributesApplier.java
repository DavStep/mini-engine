package engine.uihotswap.sui.appliers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import engine.uihotswap.sui.SUIAttributeApplier;
import engine.uihotswap.sui.SUIReaderUtils;

public class SUIActorAttributesApplier<T extends Actor> extends ASUIAttributesApplier<T> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers) {
        // scale readers
        SUIReaderUtils.addFloat(attributeAppliers, "scaleX", Actor::setScaleX);
        SUIReaderUtils.addFloat(attributeAppliers, "scaleY", Actor::setScaleX);
        attributeAppliers.put("scale", (actor, value) -> {
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                actor.setScale(values[0]);
            } else if (values.length == 2) {
                actor.setScale(values[0], values[1]);
            } else {
                Gdx.app.error("SUIActorAttributesApplier", "scale requires 1 or 2 values, got: " + values.length);
            }
        });

        // size readers
        SUIReaderUtils.addFloat(attributeAppliers, "width", Actor::setWidth);
        SUIReaderUtils.addFloat(attributeAppliers, "height", Actor::setHeight);
        attributeAppliers.put("size", (actor, value) -> {
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                actor.setSize(values[0], values[0]);
            } else if (values.length == 2) {
                actor.setSize(values[0], values[1]);
            } else {
                Gdx.app.error("SUIActorAttributesApplier", "size requires 1 or 2 values, got: " + values.length);
            }
        });

        // rotation reader
        SUIReaderUtils.addFloat(attributeAppliers, "rotation", Actor::setRotation);

        // position reader
        attributeAppliers.put("position", (actor, value) -> {
            final float[] values = SUIReaderUtils.parseFloats(value);
            if (values.length == 1) {
                actor.setPosition(values[0], values[0]);
            } else if (values.length == 2) {
                actor.setPosition(values[0], values[1]);
            } else {
                Gdx.app.error("SUIActorAttributesApplier", "position requires 1 or 2 values, got: " + values.length);
            }
        });
    }
}
