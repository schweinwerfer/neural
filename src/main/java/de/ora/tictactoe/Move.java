package de.ora.tictactoe;

public class Move {
    private Board board;
    private Coordinate move;


    public Move(final Board board, final Coordinate move) {
        this.board = board.copy();
        this.move = move;
    }
}
