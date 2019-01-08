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
}
