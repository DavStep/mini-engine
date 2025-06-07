package engine.uihotswap.sui.appliers;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.ObjectMap;
import engine.uihotswap.sui.SUIAttributeApplier;
import engine.uihotswap.sui.SUIReaderUtils;

public class SUIWidgetGroupAttributes<T extends WidgetGroup> extends SUIActorAttributesApplier<T> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers) {
        super.initAttributeApplier(attributeAppliers);
        SUIReaderUtils.addBoolean(attributeAppliers, "fillParent", WidgetGroup::setFillParent);
    }
}
