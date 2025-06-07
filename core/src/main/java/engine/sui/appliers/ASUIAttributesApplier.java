package engine.sui.appliers;

import com.badlogic.gdx.utils.ObjectMap;
import engine.sui.SUIAttributeApplier;

public abstract class ASUIAttributesApplier<T> {

    private final ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers = new ObjectMap<>();

    public ASUIAttributesApplier () {
        initAttributeApplier(attributeAppliers);
    }

    public void apply (T target, ObjectMap<String, String> attributes) {
        if (attributes == null) return;

        for (ObjectMap.Entry<String, String> attribute : attributes) {
            final String name = attribute.key;
            final String value = attribute.value;
            final SUIAttributeApplier<T, String> attributeApplier = attributeAppliers.get(name);
            if (attributeApplier == null) continue;
            attributeApplier.apply(target, value);
        }
    }

    public abstract void initAttributeApplier (ObjectMap<String, SUIAttributeApplier<T, String>> attributeAppliers);
}
