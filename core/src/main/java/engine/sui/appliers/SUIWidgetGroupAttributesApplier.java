package engine.sui.appliers;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.ObjectMap;
import engine.sui.SUIAttributeApplier;
import engine.sui.components.SUIWidgetGroup;

public final class SUIWidgetGroupAttributesApplier extends ASUIAttributesApplier<SUIWidgetGroup> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<SUIWidgetGroup, String>> attributeAppliers) {
        attributeAppliers.put("fillParent", (target, value) -> {
            final WidgetGroup view = target.getView();
            view.setFillParent(Boolean.valueOf(value));
        });
    }
}
