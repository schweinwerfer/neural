package de.ora.tictactoe;

public class Coordinate {
    int row;
    int column;

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
}
