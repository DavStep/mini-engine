package engine.uihotswap.sui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.bootcamp.demo.managers.API;
import engine.uihotswap.sui.SUIManager;
import lombok.Getter;

public class SUITable extends ASUIComponent<Table> {

    @FunctionalInterface
    private interface TableSpecialAttributesApplier {
        void apply (Table table, XmlReader.Element child);
    }

    private static final ObjectMap<String, TableSpecialAttributesApplier> specialAttributes = new ObjectMap<>();
    static {
        specialAttributes.put("row", (table, child) -> {
            table.row();
        });
        specialAttributes.put("defaults", (table, child) -> {
            API.get(SUIManager.class).getCellAttributesApplier().apply(table.defaults(), child.getAttributes());
        });
    }

    @Getter
    private final Table actor = new Table();

    @Override
    public void initFromXml (XmlReader.Element xml) {
        super.initFromXml(xml);
        populateChildren(xml);
    }

    @Override
    protected void initAttributes (XmlReader.Element xml) {
        API.get(SUIManager.class).getTableAttributesApplier().apply(actor, xml.getAttributes());
    }

    private void populateChildren (XmlReader.Element xml) {
        final int childCount = xml.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final XmlReader.Element child = xml.getChild(i);

            final String name = child.getName();
            if (specialAttributes.containsKey(child.getName())) {
                specialAttributes.get(name).apply(actor, child);
                continue;
            }

            final ASUIComponent<?> childComponent = API.get(SUIManager.class).createElement(child);
            final Actor childActor = childComponent.getActor();

            if (child.hasAttribute("absolute")) {
                actor.addActor(childActor);
            } else {
                final Cell<Actor> cell = actor.add(childActor);
                API.get(SUIManager.class).getCellAttributesApplier().apply(cell, child.getAttributes());
            }
        }
    }
}
