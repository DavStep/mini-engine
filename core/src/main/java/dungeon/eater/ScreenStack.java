package dungeon.eater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import dungeon.eater.presenters.UIScreen;

public final class ScreenStack implements com.badlogic.gdx.Screen {

    private final UIScreen ui;
    private final GameScreen game;

    public ScreenStack () {
        // init game and ui
        ui = new UIScreen();
        game = new GameScreen();
    }

    @Override
    public void show () {
        // if needed, set an input multiplexer to handle both stages
        final InputMultiplexer inputProcessor = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor.addProcessor(game.getStage());
        inputProcessor.addProcessor(ui.getStage());
    }

    @Override
    public void render (final float delta) {
        // clear screen
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        game.render(delta);
        ui.render(delta);
    }

    @Override
    public void resize (final int width, final int height) {
        game.resize(width, height);
        ui.resize(width, height);
    }

    @Override
    public void pause () {
        System.out.println("Game paused.");
        game.pause();
        ui.pause();
    }

    @Override
    public void resume () {
        System.out.println("Game resumed.");
        game.resume();
        ui.resume();
    }

    @Override
    public void hide () {
        final InputMultiplexer inputProcessor = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor.removeProcessor(game.getStage());
        inputProcessor.removeProcessor(ui.getStage());

        game.hide();
        ui.hide();
    }

    @Override
    public void dispose () {
        game.dispose();
        ui.dispose();
    }
}
