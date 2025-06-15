package engine.sui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import engine.sui.appliers.*;
import engine.sui.components.SUIActor;
import engine.sui.components.SUITable;
import lombok.Getter;

public class SUIManager {
    @Getter
    private final SUICellAttributesApplier cellAttributesApplier = new SUICellAttributesApplier();
    @Getter
    private final SUIActorAttributesApplier actorAttributesApplier = new SUIActorAttributesApplier();
    @Getter
    private final SUIWidgetGroupAttributesApplier widgetGroupAttributesApplier = new SUIWidgetGroupAttributesApplier();
    @Getter
    private final SUITableAttributesApplier tableAttributesApplier = new SUITableAttributesApplier();

    public final ObjectMap<String, Class<? extends SUIActor>> componentClasses = new ObjectMap<>();

    public SUIManager() {
        componentClasses.put("table", SUITable.class);
    }

    public SUIActor createElement (XmlReader.Element xml) {
        final Class<? extends SUIActor> componentClass = componentClasses.get(xml.getName());
        if (componentClass == null) {
            throw new IllegalArgumentException("No component for tag: " + xml.getName());
        }
        try {
            final SUIActor component = ClassReflection.newInstance(componentClass);
            component.initFromXml(xml);
            return component;
        } catch (ReflectionException e) {
            Gdx.app.error("SUIManager", "Can't instantiate " + componentClass.getName(), e);
            throw new IllegalStateException("Failed to create " + componentClass.getSimpleName(), e);
        }
    }
}
