package com.bootcamp.demo.gridLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import lombok.Setter;

public class GridLayout extends Table {
    private int rows = 0;
    private int cols = 0;

    private final Cell[][] grid;
    private final Actor[][] actors;

    @Setter @Getter
    private DefaultCellFactory defaultCell = null;

    public GridLayout(int cols, int rows) {
        this.rows = rows;
        this.cols = cols;

        this.grid = new Cell[rows][cols];
        this.actors = new Actor[rows][cols];
    }

    public void bind(){
        clear();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Actor actor = actors[i][j];

                grid[i][j] = add(actor != null ? actor : defaultCell.create());
            }
            row();
        }
    }

    public <T extends Actor> Cell<T> getCell(int x, int y) {
        validateBounds(x, y);
        return grid[y][x];
    }

    public void setCell(int x, int y, Actor actor) {
        validateBounds(x, y);

        actors[y][x] = actor;
    }

    public <T extends Actor> Cell<T> updateCell (int x, int y, Actor actor) {
        validateBounds(x, y);

        actors[y][x] = actor;

        bind();

        return grid[y][x];
    }

    private void validateBounds(int x, int y) {
        if(x < 0 || x >= cols || y < 0 || y >= rows){
            throw new IndexOutOfBoundsException();
        }
    }
}
