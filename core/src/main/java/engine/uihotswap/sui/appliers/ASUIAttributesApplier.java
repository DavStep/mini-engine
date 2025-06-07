package engine.uihotswap.sui.appliers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import engine.uihotswap.sui.SUIAttributeApplier;

public abstract class ASUIAttributesApplier<T> {

    private final ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers = new ObjectMap<>();

    public ASUIAttributesApplier () {
        initAttributeApplier(attributeAppliers);
    }

    public void apply (T obj, ObjectMap<String, String> attributes) {
        if (attributes == null) return;

        for (ObjectMap.Entry<String, String> attribute : attributes) {
            final String name = attribute.key;
            final String value = attribute.value;
            final SUIAttributeApplier<T, String> attributeApplier = attributeAppliers.get(name);
            if (attributeApplier == null) {
                Gdx.app.error("ASUIAttributesApplier", "No attribute applier found for value: " + value);
                continue;
            }
            attributeApplier.apply(obj, value);
        }
    }

    public abstract void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers);
}
