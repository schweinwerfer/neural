package de.ora.tictactoe;

public class ThreeXThreeBoard extends Board {
    public ThreeXThreeBoard(Board other) {
        super(other);
    }

    public ThreeXThreeBoard() {
        super(3, 3);
    }

    @Override
    protected Player findDiagonalWinner() {
        int currentCode = Player.NONE.getCode();
        int currentCnt = 0;

        for (int row = 0; row < board.data.length; row++) {
            Double value = board.getRawCell(row, row);
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

        for (int row = 0; row < board.data.length; row++) {
            Double value = board.getRawCell(row, 2 - row);
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

        return Player.NONE;
    }

    @Override
    public Board copy() {
        return new ThreeXThreeBoard(this);
    }
}
