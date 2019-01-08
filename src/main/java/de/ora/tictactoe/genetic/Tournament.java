package de.ora.tictactoe.genetic;

import de.ora.tictactoe.*;
import de.ora.util.Stopwatch;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Tournament {
    private static final Logger LOG = Logger.getLogger(Tournament.class.getSimpleName());
    private static final int POPULATION_SIZE = 1000;
    private List<PlayingAgent> population = new ArrayList<>();
    int epoch = 0;
    private double avgFitness;
    private Random rnd;

    public Tournament() {
        rnd = new Random();
    }

    public static void main(String[] args) throws IOException {
        Tournament tournament = new Tournament();
        tournament.createPopulation(POPULATION_SIZE);
        tournament.start();
    }

    private void start() throws IOException {
        boolean isRunning = true;

        while (isRunning) {
            int gamesPlayed = 0;
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

            // pick 20 of the best
            List<PlayingAgent> breedPool = new ArrayList<>();
            Iterator<PlayingAgent> iterator = population.iterator();
            int pickCnt = 100;

            while (pickCnt > 0 && iterator.hasNext()) {
                PlayingAgent picked = iterator.next();
                iterator.remove();
                breedPool.add(picked);
                pickCnt--;
            }

            // pick 5 from the rest randomly
            pickCnt = 10;
            while (pickCnt > 0) {
                PlayingAgent playingAgent = population.get(rnd.nextInt(population.size()));
                if (!breedPool.contains(playingAgent)) {
                    breedPool.add(playingAgent);
                    pickCnt--;
                }
            }

            population.clear();

            // breed
            int lastWinner = -1;
            for (int i = 0; i < breedPool.size(); i++) {
                for (int j = 0; j < breedPool.size(); j++) {
                    if (population.size() >= POPULATION_SIZE) {
                        break;
                    }

                    boolean breed = rnd.nextBoolean();
                    if (i != j && breed) {
                        population.add(new PlayingAgent(breedPool.get(i), breedPool.get(j)));
                    }
                    if (lastWinner != i && i < 100) {
                        population.add(breedPool.get(i)); // add old winners
                        lastWinner = i;
                    }
                }
            }

            for (PlayingAgent playingAgent : population) {
                playingAgent.resetScore();
            }

            fillPopulation(POPULATION_SIZE);
        }
        LOG.info("Tournament done.");
        File agentDir = new File("tournament/agents/");
        agentDir.mkdirs();
        for (int i = 0; i < 10; i++) {
            PlayingAgent agent = population.get(i);
            agent.store(agentDir);
        }
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

    private void createPopulation(int size) {
        for (int i = 0; i < size; i++) {
            population.add(new PlayingAgent("a" + i));
        }
    }

    private void fillPopulation(int size) {
        int diff = population.size() - size;
        if (diff >= 0) {
            return;
        }
        diff = Math.abs(diff);

        for (int i = 0; i < diff; i++) {
            population.add(new PlayingAgent("a" + i));
        }
    }
}
