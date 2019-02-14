package de.ora.tictactoe;

public class Coordinate {
    int row;
    int column;

    protected Coordinate() {
    }

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "(" + row +
                "," + column +
                ')';
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * @param rows number of rows of the {@link de.ora.neural.core.net.GenericMatrix}
     */
    public Coordinate rotate(int rows) {
        return new Coordinate(column, rows - 1 - row);
    }

    /**
     * @param columns number of columns of the {@link de.ora.neural.core.net.GenericMatrix}
     */
    public Coordinate mirrorX(int columns) {
        return new Coordinate(row, columns - 1 - column);
    }
}
