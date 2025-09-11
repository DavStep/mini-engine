package dungeon.eater.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class AxisRenderer {
    private final Color xAxisColor = Color.valueOf("#808080");
    private final Color yAxisColor = Color.valueOf("#808080");

    private final OrthographicCamera camera;

    public AxisRenderer (OrthographicCamera camera) {
        this.camera = camera;
    }

    public void render (ShapeRenderer shape) {
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
