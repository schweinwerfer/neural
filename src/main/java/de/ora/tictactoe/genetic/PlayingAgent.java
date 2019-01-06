package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.Player;

import java.util.*;

public class PlayingAgent {
    private String name;
    private int score = 0;
    private Map<Board, List<Coordinate>> movesAsPlayer1 = new HashMap<>();
    private Map<Board, List<Coordinate>> movesAsPlayer2 = new HashMap<>();

    public PlayingAgent(String name) {
        this.name = name;
    }

    public Coordinate play(final Player player, final Board board) {
        Map<Board, List<Coordinate>> moves = null;

        switch (player) {
            case PLAYER1:
                moves = this.movesAsPlayer1;
                break;
            case PLAYER2:
                moves = this.movesAsPlayer2;
                break;
            default:
                throw new IllegalArgumentException("Invalid player: " + player);
        }

        List<Coordinate> possibleMoves = moves.get(board);
        if (possibleMoves == null) {
            possibleMoves = new ArrayList<>();
            moves.put(board, possibleMoves);
        }

        if (possibleMoves.isEmpty()) {
            List<Coordinate> freeCells = board.freeCells();
            // choose randomly one move
            possibleMoves.add(freeCells.get(new Random().nextInt(freeCells.size())));
        }

        return possibleMoves.get(0);
    }

    public void feedback(int score) {
        this.score += score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
