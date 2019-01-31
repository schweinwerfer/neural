package de.ora.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeXThreeBoard extends Board {
    public ThreeXThreeBoard(Board other) {
        super(other);
    }

    public ThreeXThreeBoard() {
        super(3, 3);
    }

    @Override
    protected Player findDiagonalWinner() {
        Map<String, List<Player>> diagonalSequences = new HashMap<>();
        List<Player> list;

        for (int row = 0; row < board.data.length; row++) {
            // full diagonal 1
            list = diagonalSequences.computeIfAbsent("c=r", k -> new ArrayList<>());
            list.add(Player.from(board.getRawCell(row, row)));

            // full diagonal 2
            list = diagonalSequences.computeIfAbsent("c=2-r", k -> new ArrayList<>());
            list.add(Player.from(board.getRawCell(row, 2 - row)));
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
        return new ThreeXThreeBoard(this);
    }
}
