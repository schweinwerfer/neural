package de.ora.tictactoe;

import de.ora.neural.core.net.GenericMatrix;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public abstract class Board {
    private int dimension;
    protected int winCnt;
    protected GenericMatrix<Integer> board;
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
        board = new GenericMatrix<>(dimension, dimension).initWith(Player.NONE.getCode());
        playerToPiecesCnt = new HashMap<>();
        activePlayer = Player.PLAYER1;
    }

    public List<Coordinate> freeCells() {
        List<Coordinate> result = new ArrayList<>();
        for (int y = 0; y < board.data.length; y++) {
            Object[] row = board.getRow(y);
            for (int x = 0; x < row.length; x++) {
                Integer value = (Integer) row[x];
                if (value == 0) {
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
        for (int c = 0; c < board.getColumnCount(); c++) {
            currentCode = Player.NONE.getCode();
            currentCnt = 0;
            for (Object value : board.getColumn(c)) {
                Integer intValue = (Integer) value;
                if (intValue != Player.NONE.getCode()) {
                    if (intValue.intValue() == currentCode) {
                        currentCnt++;
                        if (currentCnt == winCnt) {
                            return Player.from(currentCode);
                        }
                    } else {
                        currentCode = intValue.intValue();
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
        for (Object[] row : board.getRows()) {
            currentCode = Player.NONE.getCode();
            currentCnt = 0;
            for (Object value : row) {
                Integer intValue = (Integer) value;
                if (intValue != Player.NONE.getCode()) {
                    if (intValue.intValue() == currentCode) {
                        currentCnt++;
                        if (currentCnt == winCnt) {
                            return Player.from(currentCode);
                        }
                    } else {
                        currentCode = intValue.intValue();
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
        Integer existing = board.set(row, column, activePlayer.getCode());
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
        StringBuilder sb = new StringBuilder().append(activePlayer.name()).append(System.lineSeparator());
        for (Object[] row : this.board.getRows()) {
            for (Object cell : row) {
                sb.append(cell).append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(System.lineSeparator());
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String asKey() {
        StringBuilder sb = new StringBuilder().append(activePlayer.getCode()).append("|");
        sb.append(this.board.getRowCount()).append(',').append(this.board.getColumnCount()).append("|");
        for (Object[] row : this.board.getRows()) {
            for (Object cell : row) {
                sb.append(cell).append(',');
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    public static Board fromKey(final String key) {
        Board result = new ThreeXThreeBoard();
        String[] parts = StringUtils.split(key, "|");
        Player player = Player.from(Integer.valueOf(parts[0]));
        String[] dimensionsString = parts[1].split(",");
        int rows = Integer.valueOf(dimensionsString[0]);
        int columns = Integer.valueOf(dimensionsString[1]);
        String[] cellsString = parts[2].split(",");

        int i = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                Integer value = Integer.valueOf(cellsString[i++]);
                result.board.set(r, c, value);
                Player activePlayer = Player.from(value);
                if (activePlayer != Player.NONE) {
                    result.playerToPiecesCnt.putIfAbsent(activePlayer, 0);
                    result.playerToPiecesCnt.computeIfPresent(activePlayer, (k, v) -> ++v);
                }
            }
        }

        result.setActivePlayer(player);

        return result;
    }

    private void setActivePlayer(Player player) {
        this.activePlayer = player;
    }

    public abstract Board copy();

    public boolean set(final Coordinate move) {
        return this.set(move.row, move.column);
    }

    public Integer get(int row, int column) {
        return board.getRawCell(row, column);
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
