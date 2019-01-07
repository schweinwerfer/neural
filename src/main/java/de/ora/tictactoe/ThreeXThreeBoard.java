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
            int intValue = value.intValue();
            if (intValue == Player.NONE.getCode()) {
                currentCode = Player.NONE.getCode();
                currentCnt = 0;
                continue;
            }
            if (intValue == currentCode) {
                currentCnt++;
                if (currentCnt == winCnt) {
                    return Player.from(currentCode);
                }
            } else {
                currentCode = intValue;
                currentCnt = 1;
            }
        }

        currentCode = Player.NONE.getCode();
        currentCnt = 0;

        for (int row = 0; row < board.data.length; row++) {
            Double value = board.getRawCell(row, 2 - row);
            int intValue = value.intValue();
            if (intValue == Player.NONE.getCode()) {
                currentCode = Player.NONE.getCode();
                currentCnt = 0;
                continue;
            }
            if (intValue == currentCode) {
                currentCnt++;
                if (currentCnt == winCnt) {
                    return Player.from(currentCode);
                }
            } else {
                currentCode = intValue;
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
