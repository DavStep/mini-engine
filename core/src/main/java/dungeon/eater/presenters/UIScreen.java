package dungeon.eater.presenters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import dungeon.eater.dialogs.TestDialog;
import dungeon.eater.dialogs.core.DialogManager;
import dungeon.eater.events.core.EventListener;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.managers.API;
import dungeon.eater.pages.TestPage;
import dungeon.eater.pages.core.APage;
import dungeon.eater.pages.core.PageManager;
import engine.Squircle;
import lombok.Getter;
import lombok.Setter;

public class UIScreen extends ScreenAdapter implements Disposable, EventListener {

    @Getter
    private final Stage stage;
    @Getter
    private final Table rootUI;
    @Getter
    private Cell<APage> mainPageCell;

    @Getter @Setter
    private boolean buttonPressed;

    public UIScreen () {
        final OrthographicCamera camera = new OrthographicCamera();
        final float aspect = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        final float decisionAspect = 2560.0f / 1440.0f;

        final float width, height;
        if (aspect < decisionAspect) {
            // aspect ratio lower than mid-aspect, usually better to fix width
            width = 1440;
            height = aspect * width;
        } else {
            // aspect ratio lower than mid-aspect, usually better to fix height
            height = 2560;
            width = height * aspect;
        }

        rootUI = new Table();
        rootUI.setFillParent(true);

        // init stage
        stage = new Stage(new ExtendViewport(width, height, camera));
        stage.addActor(rootUI);

        // construct
        mainPageCell = rootUI.add().grow();

        final Table table = new Table();
        table.setBackground(Squircle.getSquircle(30, Color.BLUE));
        rootUI.add(table).size(300).expand().top().right();

        API.Instance().register(UIScreen.class, this);
        API.get(EventModule.class).registerListener(this);

        API.get(PageManager.class).show(TestPage.class);
    }

    @Override
    public void render (float delta) {
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            final DialogManager dialogManager = API.get(DialogManager.class);
            final TestDialog testDialog = dialogManager.getDialog(TestDialog.class);
            if (testDialog.isShown()) {
                dialogManager.hide(TestDialog.class);
            } else {
                dialogManager.show(TestDialog.class);
            }
        }
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {
        stage.dispose();
    }
}
