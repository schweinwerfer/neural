package de.ora.tictactoe;

import de.ora.neural.core.net.Matrix;

import java.util.*;

public abstract class Board {
    private int dimension;
    protected int winCnt;
    protected Matrix board;
    private Player activePlayer;
    private Map<Player, Integer> playerToPiecesCnt = new HashMap<>();

    protected Board(final Board other) {
        this.dimension = other.dimension;
        this.winCnt = other.winCnt;
        this.activePlayer = other.activePlayer;
        this.board = other.board.copy();
        this.playerToPiecesCnt = other.playerToPiecesCnt;
    }

    protected Board(int dimension, int winCnt) {
        this.dimension = dimension;
        this.winCnt = winCnt;
        reset();
    }

    public void reset() {
        board = new Matrix(dimension, dimension).initWith(Player.NONE.getCode());
        playerToPiecesCnt = new HashMap<>();
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
        if (winnerCheckNotNeeded()) {
            return Player.NONE;
        }
        int currentCode = Player.NONE.getCode();
        int currentCnt = 0;

        // columns
        for (int c = 0; c < board.getColumns(); c++) {
            currentCode = Player.NONE.getCode();
            currentCnt = 0;
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
                } else {
                    currentCode = Player.NONE.getCode();
                    currentCnt = 0;
                }
            }
        }

        currentCode = Player.NONE.getCode();
        currentCnt = 0;

        // rows
        for (double[] row : board.data) {
            currentCode = Player.NONE.getCode();
            currentCnt = 0;
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
                } else {
                    currentCode = Player.NONE.getCode();
                    currentCnt = 0;
                }
            }
        }

        // diagonals
        return findDiagonalWinner();
    }

    private boolean winnerCheckNotNeeded() {
        Integer player1Cnt = this.playerToPiecesCnt.getOrDefault(Player.PLAYER1, 0);
        Integer player2Cnt = this.playerToPiecesCnt.getOrDefault(Player.PLAYER2, 0);
        return player1Cnt < winCnt && player2Cnt < winCnt;
    }

    public int getPlayedPiecesCount() {
        int sum = 0;
        for (Integer cnt : playerToPiecesCnt.values()) {
            if (cnt == null) {
                continue;
            }

            sum += cnt;
        }

        return sum;
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
            this.playerToPiecesCnt.putIfAbsent(activePlayer, 0);
            this.playerToPiecesCnt.computeIfPresent(activePlayer, (key, value) -> ++value);
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

    public String asKey() {
        StringBuilder sb = new StringBuilder(activePlayer.name()).append(System.lineSeparator());
        for (double[] row : this.board.data) {
            for (double cell : row) {
                sb.append(cell).append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(System.lineSeparator());
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public abstract Board copy();

    public boolean set(final Coordinate move) {
        return this.set(move.row, move.column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board1 = (Board) o;
        return dimension == board1.dimension &&
                winCnt == board1.winCnt &&
                board.equals(board1.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, winCnt, board);
    }

    public Coordinate findWinningMoveForPlayer(final Player player) {
        if (playerToPiecesCnt.getOrDefault(player, 0) >= (winCnt - 1)) {

        }

        return null;
    }
}
