package engine.sui.components;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.ObjectMap;
import com.bootcamp.demo.managers.API;
import engine.sui.SUIManager;
import lombok.Getter;

public class SUIWidgetGroup<T extends WidgetGroup> extends SUIActor<T> {

    @Getter
    protected WidgetGroup view;

    @Override
    protected void initAttributes (ObjectMap<String, String> attributes) {
        super.initAttributes(attributes);
        API.get(SUIManager.class).getWidgetGroupAttributesApplier().apply(this, attributes);
    }

    @Override
    protected void initView () {
        view = new WidgetGroup();
        super.view = view;
    }
}
