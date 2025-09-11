package dungeon.eater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dungeon.eater.events.core.EventListener;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.managers.API;
import engine.Squircle;
import engine.widgets.BorderedTable;
import lombok.Getter;

/**
 * coordinate plane renderer
 * - 1 world unit = 1 tile (1x1 squares)
 * - pan with arrow keys, zoom with Q/E, R to recenter at (0,0)
 * - G toggles unit grid, A toggles axes
 */
public class GameScreen extends ScreenAdapter implements Disposable, EventListener {

    private final ShapeRenderer shapes;

    @Getter
    private final Stage stage;
    private final OrthographicCamera camera;

    // toggles
    private boolean showGrid = true;
    private boolean showAxes = true;

    // camera controls
    private final float panSpeed = 100f;   // world units per second
    private final float zoomStep = 0.1f;  // per key press

    private final AxisRenderer axisRenderer = new AxisRenderer();
    private final GridRenderer gridRenderer = new GridRenderer();

    public GameScreen () {
        camera = new OrthographicCamera();
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

        API.Instance().register(GameScreen.class, this);
        API.get(EventModule.class).registerListener(this);

        stage = new Stage(new ExtendViewport(width, height, camera));
        shapes = new ShapeRenderer();

        final Table table = new Table();
        table.setBackground(Squircle.getSquircle(30, Color.RED));
        table.setSize(300, 300);
        stage.addActor(table);

        stage.addListener(new DragPanController());

        Gdx.app.postRunnable(this::centerCamera);
    }

    private void centerCamera () {
        stage.getCamera().position.set(0f, 0f, 0f);
        if (stage.getCamera() instanceof OrthographicCamera oc) {
            oc.zoom = 1f; // 1 world unit = 1 screen unit per viewport scaling
            oc.update();
        } else {
            stage.getCamera().update();
        }
    }

    @Override
    public void render (float delta) {
        handleInput(delta);

        stage.act(delta);
        stage.draw();

        final OrthographicCamera cam = (OrthographicCamera) stage.getCamera();
        shapes.setProjectionMatrix(cam.combined);

        if (showGrid) {
            gridRenderer.render(shapes);
        }
        if (showAxes) {
            axisRenderer.render(shapes);
        }
    }

    private void handleInput (float delta) {
        // toggle visuals
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) showGrid = !showGrid;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) showAxes = !showAxes;
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) centerCamera();

        // zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) camera.zoom = MathUtils.clamp(camera.zoom + zoomStep, 0.1f, 10f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) camera.zoom = MathUtils.clamp(camera.zoom - zoomStep, 0.1f, 10f);

        // pan
        float speed = panSpeed * camera.zoom * delta; // scale by zoom so panning feels consistent
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.position.x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.position.x += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.position.y -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.position.y += speed;

        camera.update();
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
//        gridRenderer.resize(width, height);
    }

    @Override
    public void dispose () {
        stage.dispose();
        shapes.dispose();
    }

    private class AxisRenderer {

        private final Color xAxisColor = Color.valueOf("#808080");
        private final Color yAxisColor = Color.valueOf("#808080");

        private void render (ShapeRenderer shape) {
            shape.setProjectionMatrix(camera.combined);

            // view bounds in world units
            float width = camera.viewportWidth * camera.zoom;
            float height = camera.viewportHeight * camera.zoom;
            float leftBorderPos = camera.position.x - width / 2f;
            float rightBorderPos = camera.position.x + width / 2f;
            float bottomBorderPos = camera.position.y - height / 2f;
            float topBorderPos = camera.position.y + height / 2f;

            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(xAxisColor);
            shape.line(0f, bottomBorderPos, 0f, topBorderPos);
            shape.setColor(yAxisColor);
            shape.line(leftBorderPos, 0f, rightBorderPos, 0f);
            shape.end();
        }
    }

    private class DragPanController extends ClickListener {
        private boolean dragging = false;
        private int activePointer = -1;
        private int lastScreenX, lastScreenY;
        private final Vector3 tmp = new Vector3();

        public DragPanController () {
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

            final Viewport viewport = stage.getViewport();

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
            final OrthographicCamera cam = (OrthographicCamera) stage.getCamera();
            final float oldZoom = cam.zoom;
            final float factor = 1f + (amountY * 0.1f); // 10% per notch
            final float newZoom = MathUtils.clamp(oldZoom * factor, 0.1f, 10f);
            if (Math.abs(newZoom - oldZoom) < 1e-6f) return false;

            // zoom around mouse position: keep the world point under cursor stationary
            screenToWorld(Gdx.input.getX(), Gdx.input.getY());
            cam.zoom = newZoom;
            cam.update();
            return true;
        }

        private void screenToWorld (float screenX, float screenY) {
            tmp.set(screenX, screenY, 0f);
            final int x = stage.getViewport().getScreenX();
            final int y = stage.getViewport().getScreenY();
            final int width = stage.getViewport().getScreenWidth();
            final int height = stage.getViewport().getScreenHeight();
            stage.getCamera().unproject(tmp, x, y, width, height);
        }
    }

    private class GridRenderer {
        private final Color patternOne = Color.valueOf("#212121");
        private final Color patternTwo = Color.valueOf("#2c2c2c");

        private final int tileWidth = 100;
        private final int tileHeight = 100;

        private int startCol;
        private int endCol;
        private int startRow;
        private int endRow;

        private void render (ShapeRenderer shape) {
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Filled);

            recalculate();

            // loop through only the visible tiles
            for (int col = startCol; col <= endCol; col++) {
                for (int row = startRow; row <= endRow; row++) {
                    shape.setColor(getColor(col, row));
                    shape.rect(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                }
            }

            shape.end();
        }

        private void recalculate () {
            // calculate the camera's visible area (view frustum) in world units.
            final float width = camera.viewportWidth * camera.zoom;
            final float height = camera.viewportHeight * camera.zoom;
            final float halfWidth = width * 0.5f;
            final float halfHeight = height * 0.5f;

            // calculate the start and end tile indices that are visible.
            // math.floor is used to correctly handle negative camera positions.
            this.startCol = (int) Math.floor((camera.position.x - halfWidth) / tileWidth);
            this.endCol = (int) Math.floor((camera.position.x + halfWidth) / tileWidth);
            this.startRow = (int) Math.floor((camera.position.y - halfHeight) / tileHeight);
            this.endRow = (int) Math.floor((camera.position.y + halfHeight) / tileHeight);
        }

        private Color getColor (int col, int row) {
            return ((col + row) & 1) == 0 ? patternOne : patternTwo;
        }
    }
}
