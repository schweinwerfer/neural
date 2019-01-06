package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.Player;
import de.ora.tictactoe.ThreeXThreeBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tournament {
    private List<PlayingAgent> population = new ArrayList<>();

    public Tournament() {
    }

    public static void main(String[] args) {
        Tournament tournament = new Tournament();
        tournament.createPopulation(100);
        tournament.start();
    }

    private void start() {
        Board board = new ThreeXThreeBoard();
        for (int i = 0; i < population.size(); i++) {
            PlayingAgent playingAgent = population.get(i);
            for (int j = 0; j < population.size(); j++) {
                if (i != j) {
                    PlayingAgent opponent = population.get(j);

                    Player winner = play(board, playingAgent, opponent);

                    switch (winner) {
                        case PLAYER1:
                            playingAgent.feedback(1);
                            opponent.feedback(-1);
                            break;
                        case PLAYER2:
                            playingAgent.feedback(-1);
                            opponent.feedback(1);
                            break;
                        case NONE:
                            playingAgent.feedback(0);
                            opponent.feedback(0);
                            break;
                    }

                }
            }
        }

        Collections.sort(population, new Comparator<PlayingAgent>() {
            @Override
            public int compare(PlayingAgent o1, PlayingAgent o2) {
                Integer score1 = o1.getScore();
                Integer score2 = o2.getScore();

                return score1.compareTo(score2);
            }
        });
    }

    private Player play(Board board, PlayingAgent playingAgent, PlayingAgent opponent) {
        board.reset();
        while (!board.freeCells().isEmpty()) {
            Coordinate move = playingAgent.play(board.activePlayer(), board);
            boolean valid = board.set(move);
            if (!valid) {
                playingAgent.feedback(-2);
            }
            Player winner = board.findWinner();
            if (winner != Player.NONE) {
                return winner;
            }
            move = opponent.play(board.activePlayer(), board);
            valid = board.set(move);
            if (!valid) {
                opponent.feedback(-2);
            }
            winner = board.findWinner();
            if (winner != Player.NONE) {
                return winner;
            }
        }

        return Player.NONE;
    }

    private void createPopulation(int size) {
        for (int i = 0; i < 100; i++) {
            population.add(new PlayingAgent("a" + i));
        }
    }
}
