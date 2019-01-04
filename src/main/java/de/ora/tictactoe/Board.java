package de.ora.tictactoe;

import de.ora.neural.core.net.Matrix;

import java.util.ArrayList;
import java.util.List;

public abstract class Board {
    private int dimension;
    protected int winCnt;
    protected Matrix board;
    private Player activePlayer;

    protected Board(int dimension, int winCnt) {
        this.dimension = dimension;
        this.winCnt = winCnt;
        reset();
    }

    public void reset() {
        board = new Matrix(dimension, dimension).initWith(Player.NONE.getCode());
        activePlayer = Player.PLAYER1;
    }

    public List<Coordinate> freeCells() {
        List<Coordinate> result = new ArrayList<>();
        for (int y = 0; y < board.data.length; y++) {
            double[] row = board.data[y];
            for (int x = 0; x < row.length; x++) {
                if (row[x] == 0) {
                    result.add(new Coordinate(y, x));
                }
            }
        }

        return result;
    }

    public Player findWinner() {
        int currentCode = Player.NONE.getCode();
        int currentCnt = 0;

        // columns
        for (int c = 0; c < board.getColumns(); c++) {
            for (Double value : board.getColumn(c).getData()) {
                if (value != Player.NONE.getCode()) {
                    if (value.intValue() == currentCode) {
                        currentCnt++;
                        if (currentCnt == winCnt) {
                            return Player.from(currentCode);
                        }
                    } else {
                        currentCode = value.intValue();
                        currentCnt = 1;
                    }
                }
            }
        }

        currentCode = Player.NONE.getCode();
        currentCnt = 0;

        // rows
        for (double[] row : board.data) {
            for (Double value : row) {
                if (value != Player.NONE.getCode()) {
                    if (value.intValue() == currentCode) {
                        currentCnt++;
                        if (currentCnt == winCnt) {
                            return Player.from(currentCode);
                        }
                    } else {
                        currentCode = value.intValue();
                        currentCnt = 1;
                    }
                }
            }
        }

        // diagonals
        return findDiagonalWinner();
    }

    protected abstract Player findDiagonalWinner();

    public Player activePlayer() {
        return activePlayer;
    }

    public boolean set(int row, int column) {
        double existing = board.set(row, column, activePlayer.getCode());
        if (existing != 0) {
            board.set(row, column, existing);
            return false;
        } else {
            activePlayer = activePlayer == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Active Player: ").append(activePlayer).append(System.lineSeparator());
        sb.append(board);

        return sb.toString();
    }
}
