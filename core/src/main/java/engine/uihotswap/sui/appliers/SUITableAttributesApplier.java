package engine.uihotswap.sui.appliers;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import engine.Resources;
import engine.uihotswap.sui.SUIAttributeApplier;

public class SUITableAttributesApplier<T extends Table> extends SUIWidgetGroupAttributes<T> {

    @Override
    public void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers) {
        super.initAttributeApplier(attributeAppliers);
        attributeAppliers.put("background", (target, value) -> {
            target.setBackground(Resources.getDrawable(value));
        });
    }
}
