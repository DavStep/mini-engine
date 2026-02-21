package dungeon.eater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import dungeon.eater.events.core.EventListener;
import dungeon.eater.events.core.EventModule;
import dungeon.eater.generator.DungeonGenerator;
import dungeon.eater.managers.API;
import engine.Squircle;
import engine.tools.AxisRenderer;
import engine.tools.DragPanController;
import engine.tools.GridRenderer;
import lombok.Getter;

/**
 * coordinate plane renderer
 * - 1 world unit = 1 tile (1x1 squares)
 * - pan with arrow keys, zoom with Q/E, R to recenter at (0,0)
 * - G toggles unit grid, A toggles axes
 */
public class GameScreen extends ScreenAdapter implements Disposable, EventListener {

    private static final int TILE_SIZE = 100;

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

    private final AxisRenderer axisRenderer;
    private final GridRenderer gridRenderer;
    private final DungeonGenerator dungeonGenerator;

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

        axisRenderer = new AxisRenderer(camera);
        gridRenderer = new GridRenderer(camera, TILE_SIZE, TILE_SIZE);
        final DragPanController dragPanController = new DragPanController(stage.getViewport(), camera);

        stage.addListener(dragPanController);

        Gdx.app.postRunnable(this::centerCamera);


        final Table table = new Table();
        table.setBackground(Squircle.getSquircle(30, Color.RED));
        table.setSize(300, 300);
        stage.addActor(table);

        dungeonGenerator = new DungeonGenerator();
        dungeonGenerator.generate();
    }

    private void drawRooms () {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.BLUE);
        for (DungeonGenerator.Room room : dungeonGenerator.getRooms()) {
            shapes.rect(room.x * TILE_SIZE, room.y * TILE_SIZE, room.width * TILE_SIZE, room.height * TILE_SIZE);
        }
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        for (DungeonGenerator.Room room : dungeonGenerator.getCorridors()) {
            shapes.rect(room.x * TILE_SIZE, room.y * TILE_SIZE, room.width * TILE_SIZE, room.height * TILE_SIZE);
        }
        shapes.end();
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

        final OrthographicCamera cam = (OrthographicCamera) stage.getCamera();
        shapes.setProjectionMatrix(cam.combined);

        if (showGrid) {
            gridRenderer.render(shapes);
        }
        if (showAxes) {
            axisRenderer.render(shapes);
        }

        drawRooms();

        stage.act(delta);
        stage.draw();
    }

    private void handleInput (float delta) {
        // toggle visuals
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) showGrid = !showGrid;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) showAxes = !showAxes;
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) centerCamera();

        // zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) camera.zoom = MathUtils.clamp(camera.zoom + zoomStep, 0.1f, 10f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) camera.zoom = MathUtils.clamp(camera.zoom - zoomStep, 0.1f, 10f);

        // pan
        float speed = panSpeed * camera.zoom * delta; // scale by zoom so panning feels consistent
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.position.x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.position.x += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.position.y -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.position.y += speed;

        // commands
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) dungeonGenerator.generate();


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

}
