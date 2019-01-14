package de.ora.tictactoe.genetic;

import de.ora.tictactoe.*;
import de.ora.util.Stopwatch;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tournament extends ATournament {

    public Tournament() {
        super(new File("tournament/agents/"));
    }

    public static void main(String[] args) throws IOException {
        Tournament tournament = new Tournament();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("random")) {
                tournament.createPopulation(POPULATION_SIZE);
            } else if (args[0].equalsIgnoreCase("load")) {
                tournament.loadPopulation();
            }
        }

        tournament.start();
    }


    private void start() throws IOException {
        boolean isRunning = true;

        while (isRunning) {
            int gamesPlayed = 0;
            fillPopulation(POPULATION_SIZE);
            LOG.info("Tournament " + (++epoch) + " size: " + population.size());
            Stopwatch stopwatch = Stopwatch.createStarted();
            Board board = new ThreeXThreeBoard();

            for (int i = 0; i < population.size(); i++) {
                PlayingAgent playingAgent = population.get(i);
                for (int j = 0; j < population.size(); j++) {
                    if (i != j) {
                        PlayingAgent opponent = population.get(j);

                        Player winner = play(board, playingAgent, opponent);
                        gamesPlayed++;
                        switch (winner) {
                            case PLAYER1:
                                playingAgent.feedback(GameResult.WON);
                                opponent.feedback(GameResult.LOST);
                                break;
                            case PLAYER2:
                                playingAgent.feedback(GameResult.LOST);
                                opponent.feedback(GameResult.WON);
                                break;
                            case NONE:
                                playingAgent.feedback(GameResult.DRAW);
                                opponent.feedback(GameResult.DRAW);
                                break;
                        }

                    }
                }
            }
            LOG.info("Done @ " + stopwatch.toString() + ". Games played: " + gamesPlayed);
            // Sort best performers to start of list
            Collections.sort(population, new Comparator<PlayingAgent>() {
                @Override
                public int compare(PlayingAgent o1, PlayingAgent o2) {
                    Double score1 = o1.getFitness();
                    Double score2 = o2.getFitness();

                    return score2.compareTo(score1);
                }
            });

            // calc avg score of population
            avgFitness = 0;
            for (PlayingAgent agent : population) {
                avgFitness += agent.getFitness();
            }
            avgFitness = avgFitness / population.size();

            PlayingAgent bestAgent = population.get(0);
            if (bestAgent.getFitness() == 1.0) {
                break;
            }

            LOG.info(("Tournament " + epoch + " end. best: " + bestAgent.getDetailedScore() + " avg: " + avgFitness + " worst: " + population.get(population.size() - 1).getDetailedScore()));

            List<PlayingAgent> breedPool = pickBest();
            population.clear();
            breed(breedPool);

            for (PlayingAgent playingAgent : population) {
                playingAgent.resetScore();
            }

            fillPopulation(POPULATION_SIZE);
        }
        LOG.info("Tournament done.");
        File agentDir2 = new File("tournament/agents/ng/");
        agentDir2.mkdirs();
        for (int i = 0; i < 10; i++) {
            PlayingAgent agent = population.get(i);
            agent.store(agentDir2);
        }
        population.clear();
        fillPopulation(POPULATION_SIZE);
        start();
    }

    private Player play(Board board, PlayingAgent playingAgent, PlayingAgent opponent) {
        board.reset();
        List<Coordinate> freeCells = board.freeCells();
        while (!freeCells.isEmpty()) {
            Coordinate move = playingAgent.play(board.activePlayer(), board);
            boolean valid = board.set(move);
            if (!valid) {
                playingAgent.feedback(GameResult.LOST);
            }
            Player winner = board.findWinner();
            if (winner != Player.NONE) {
                return winner;
            }
            freeCells = board.freeCells();
            if (freeCells.isEmpty()) {
                break;
            }
            move = opponent.play(board.activePlayer(), board);
            valid = board.set(move);
            if (!valid) {
                opponent.feedback(GameResult.LOST);
            }
            winner = board.findWinner();
            if (winner != Player.NONE) {
                return winner;
            }
            freeCells = board.freeCells();
        }

        return Player.NONE;
    }


}
