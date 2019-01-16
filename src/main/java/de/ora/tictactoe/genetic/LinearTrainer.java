package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.GameResult;
import de.ora.tictactoe.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LinearTrainer extends PlayingAgent {
    private boolean initDone = false;
    private Map<String, Integer> boardKey2MoveIdx = new HashMap<>();

    public LinearTrainer() {
    }

    public LinearTrainer(Board board) {
        super("trainer");
        this.initDone = init(board, board.freeCells());
    }

    public LinearTrainer(PlayingAgent father, PlayingAgent mother) {
        throw new UnsupportedOperationException("Trainer cannot be combined!");
    }

    private boolean init(final Board board, List<Coordinate> freeCellsCurr) {
        if (this.initDone) {
            return true;
        }

        List<Coordinate> freeCells = freeCellsCurr;

        while (!(freeCells = board.freeCells()).isEmpty()) {
            String key = board.asKey();
            if (this.moves.containsKey(key)) {
                return true;
            }

            final List<Coordinate> currentFreeCells = new LinkedList<>(freeCells);
            List<Coordinate> coordinates = this.moves.computeIfAbsent(key, k ->
                    new LinkedList<>(currentFreeCells)
            );

            for (Coordinate move : coordinates) {
                Board copy = board.copy();
                boolean valid = copy.set(move);
                List<Coordinate> freeCellsAfterMove = copy.freeCells();
                boolean freeCellsLeft = !freeCellsAfterMove.isEmpty();
                boolean seekFurther = valid && freeCellsLeft && copy.findWinner() == Player.NONE;

                if (seekFurther) {
                    init(copy, freeCellsAfterMove);
                }

            }
        }

        return true;
    }

    @Override
    public Coordinate play(Player player, Board board) {
        if (!initDone) {
            throw new IllegalStateException("Not initialized!");
        }

        String boardKey = board.asKey();
        Integer idx = this.boardKey2MoveIdx.computeIfAbsent(boardKey, k -> 0);
        this.boardKey2MoveIdx.put(boardKey, idx + 1);
        List<Coordinate> possibleMoves = this.moves.get(boardKey);
        if (idx >= possibleMoves.size()) {
            return null;
        }
        return possibleMoves.get(idx);
    }

    public boolean isInitDone() {
        return initDone;
    }

    @Override
    public void feedback(GameResult result) {
    }

    public void reset() {
        this.boardKey2MoveIdx.clear();
    }
}
