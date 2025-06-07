package com.bootcamp.demo.pages;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bootcamp.demo.pages.core.APage;
import engine.uihotswap.XmlUIReader;

public class TestPage extends APage {

    @Override
    protected void constructContent (Table content) {
        final XmlUIReader uiReader = new XmlUIReader();

        final Table rootTable = uiReader.getRootTable();
        content.add(rootTable).grow();
    }
}
