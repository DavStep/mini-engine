package engine.uihotswap.sui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.bootcamp.demo.managers.API;
import engine.uihotswap.sui.SUIManager;
import lombok.Getter;
import lombok.Setter;

public class SUITable<T extends Table> extends SUIWidgetGroup<T> {

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
            final Cell<?> cell = table.defaults();
            final SUICell suiCell = new SUICell();
            suiCell.setCell(cell);
            suiCell.initFromXml(child);
        });
        specialAttributes.put("cell", (table, child) -> {
            final Cell<?> cell = table.add();
            final SUICell suiCell = new SUICell();
            suiCell.setCell(cell);
            suiCell.initFromXml(child);
        });
    }

    @Getter
    @Setter
    private Color backgroundColor = Color.WHITE;
    @Getter
    private final int[] cornerRadius = new int[4];

    @Getter
    protected Table view;

    @Override
    public void initFromXml (XmlReader.Element xml) {
        super.initFromXml(xml);
        populateChildren(xml);
    }

    @Override
    protected void initAttributes (ObjectMap<String, String> attributes) {
        super.initAttributes(attributes);
        API.get(SUIManager.class).getTableAttributesApplier().apply(this, attributes);
    }

    @Override
    protected void initView () {
        view = new Table();
        super.view = view;
    }

    private void populateChildren (XmlReader.Element xml) {
        final int childCount = xml.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final XmlReader.Element child = xml.getChild(i);

            final String name = child.getName();
            if (specialAttributes.containsKey(child.getName())) {
                specialAttributes.get(name).apply(view, child);
                continue;
            }

            final SUIActor<?> childComponent = API.get(SUIManager.class).createElement(child);
            final Actor childActor = childComponent.getView();
            view.addActor(childActor);
        }
    }
}
