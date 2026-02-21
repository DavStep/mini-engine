package dungeon.eater.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GridRenderer {
    private final Color patternOne = Color.valueOf("#212121");
    private final Color patternTwo = Color.valueOf("#2c2c2c");

    private final int tileWidth;
    private final int tileHeight;
    private final OrthographicCamera camera;

    private int startCol;
    private int endCol;
    private int startRow;
    private int endRow;

    public GridRenderer (OrthographicCamera camera, int tileWidth, int tileHeight) {
        this.camera = camera;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public void render (ShapeRenderer shape) {
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
