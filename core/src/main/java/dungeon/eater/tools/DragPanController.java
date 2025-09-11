package dungeon.eater.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DragPanController extends ClickListener {
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private boolean dragging = false;
    private int activePointer = -1;
    private int lastScreenX, lastScreenY;
    private final Vector3 tmp = new Vector3();

    public DragPanController (Viewport viewport, OrthographicCamera camera) {
        this.viewport = viewport;
        this.camera = camera;
        setTapSquareSize(0f); // avoid click drag threshold jitter
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (button != Input.Buttons.LEFT || dragging) return false;
        dragging = true;
        activePointer = pointer;
        lastScreenX = Gdx.input.getX(pointer);
        lastScreenY = Gdx.input.getY(pointer);
        return true; // capture drag
    }


    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        if (!dragging || pointer != activePointer) return;
        final int sx = Gdx.input.getX(activePointer);
        final int sy = Gdx.input.getY(activePointer);

        // convert screen-pixel delta to world-units (orthographic)
        final float worldPerPixelX = (camera.viewportWidth * camera.zoom) / Math.max(1, viewport.getScreenWidth());
        final float worldPerPixelY = (camera.viewportHeight * camera.zoom) / Math.max(1, viewport.getScreenHeight());

        final float dx = (lastScreenX - sx) * worldPerPixelX; // inverse so content follows finger/mouse
        final float dy = (sy - lastScreenY) * worldPerPixelY; // Y is inverted in screen space

        camera.position.add(dx, dy, 0f);
        camera.update();

        lastScreenX = sx;
        lastScreenY = sy;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) dragging = false;
    }

    @Override
    public boolean scrolled (InputEvent event, float x, float y, float amountX, float amountY) {
        // wheel zoom towards cursor position
        final float oldZoom = camera.zoom;
        final float factor = 1f + (amountY * 0.1f); // 10% per notch
        final float newZoom = MathUtils.clamp(oldZoom * factor, 0.1f, 10f);
        if (Math.abs(newZoom - oldZoom) < 1e-6f) return false;

        // zoom around mouse position: keep the world point under cursor stationary
        screenToWorld(Gdx.input.getX(), Gdx.input.getY());
        camera.zoom = newZoom;
        camera.update();
        return true;
    }

    private void screenToWorld (float screenX, float screenY) {
        tmp.set(screenX, screenY, 0f);
        final int x = viewport.getScreenX();
        final int y = viewport.getScreenY();
        final int width = viewport.getScreenWidth();
        final int height = viewport.getScreenHeight();
        camera.unproject(tmp, x, y, width, height);
    }
}
