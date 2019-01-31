package de.ora.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourXFourBoard extends Board {
    public FourXFourBoard(Board other) {
        super(other);
    }

    public FourXFourBoard() {
        super(4, 3);
    }

    @Override
    protected Player findDiagonalWinner() {
        Map<String, List<Player>> diagonalSequences = new HashMap<>();
        List<Player> list;
        final int dimension = getDimension();
        int column;

        for (int row = 0; row < board.data.length; row++) {
            // full diagonal 1
            list = diagonalSequences.computeIfAbsent("c=r", k -> new ArrayList<>());
            list.add(Player.from(board.getRawCell(row, row)));

            // full diagonal 2
            list = diagonalSequences.computeIfAbsent("c=3-r", k -> new ArrayList<>());
            list.add(Player.from(board.getRawCell(row, 3 - row)));

            // small diagonal 1
            column = 1 + row;
            if (column >= 0 && column < dimension) {
                list = diagonalSequences.computeIfAbsent("c=1+r", k -> new ArrayList<>());
                list.add(Player.from(board.getRawCell(row, column)));
            }

            // small diagonal 2
            column = 1 - row;
            if (column >= 0 && column < dimension) {
                list = diagonalSequences.computeIfAbsent("c=1-r", k -> new ArrayList<>());
                list.add(Player.from(board.getRawCell(row, column)));
            }

            // small diagonal 3
            column = 2 - row;
            if (column >= 0 && column < dimension) {
                list = diagonalSequences.computeIfAbsent("c=2-r", k -> new ArrayList<>());
                list.add(Player.from(board.getRawCell(row, column)));
            }

            // small diagonal 4
            column = 4 - row;
            if (column >= 0 && column < dimension) {
                list = diagonalSequences.computeIfAbsent("c=4-r", k -> new ArrayList<>());
                list.add(Player.from(board.getRawCell(row, column)));
            }
        }

        for (List<Player> moves : diagonalSequences.values()) {
            Player currentPlayer = Player.NONE;
            int currentCnt = 0;

            for (Player move : moves) {
                if (move == Player.NONE) {
                    currentPlayer = Player.NONE;
                    currentCnt = 0;
                    continue;
                }
                if (move == currentPlayer) {
                    currentCnt++;
                    if (currentCnt == winCnt) {
                        return move;
                    }
                } else {
                    currentPlayer = move;
                    currentCnt = 1;
                }
            }
        }

        return Player.NONE;
    }

    @Override
    public Board copy() {
        return new FourXFourBoard(this);
    }
}
