package de.ora.tictactoe.genetic;

import de.ora.tictactoe.*;
import de.ora.util.Stopwatch;

import java.util.*;
import java.util.logging.Logger;

public class Tournament {
    private static final Logger LOG = Logger.getLogger(Tournament.class.getSimpleName());
    private static final int POPULATION_SIZE = 500;
    private List<PlayingAgent> population = new ArrayList<>();
    int epoch = 0;
    private double avgScore;
    private double oldAvgScore;
    private Random rnd;

    public Tournament() {
        rnd = new Random();
    }

    public static void main(String[] args) {
        Tournament tournament = new Tournament();
        tournament.createPopulation(POPULATION_SIZE);
        tournament.start();
    }

    private void start() {
        LOG.info("Tournament " + (++epoch) + " size: " + population.size());
        Stopwatch stopwatch = Stopwatch.createStarted();
        Board board = new ThreeXThreeBoard();
        for (int i = 0; i < population.size(); i++) {
            PlayingAgent playingAgent = population.get(i);
            for (int j = 0; j < population.size(); j++) {
                if (i != j) {
                    PlayingAgent opponent = population.get(j);

                    Player winner = play(board, playingAgent, opponent);

                    switch (winner) {
                        case PLAYER1:
                            playingAgent.feedback(GameResult.WON, 2);
                            opponent.feedback(GameResult.LOST, -2);
                            break;
                        case PLAYER2:
                            playingAgent.feedback(GameResult.LOST, -2);
                            opponent.feedback(GameResult.WON, 2);
                            break;
                        case NONE:
                            playingAgent.feedback(GameResult.DRAW, 1);
                            opponent.feedback(GameResult.DRAW, 1);
                            break;
                    }

                }
            }
        }
        LOG.info("Done @ " + stopwatch.toString());
        // Sort best performers to start of list
        Collections.sort(population, new Comparator<PlayingAgent>() {
            @Override
            public int compare(PlayingAgent o1, PlayingAgent o2) {
                Integer score1 = o1.getScore();
                Integer score2 = o2.getScore();

                return score2.compareTo(score1);
            }
        });

        // calc avg score of population
        oldAvgScore = avgScore;
        avgScore = 0;
        for (PlayingAgent agent : population) {
            avgScore += agent.getScore() * 1.0;
        }
        avgScore = avgScore / population.size();

        LOG.info(("Tournament " + epoch + " end. best: " + population.get(0).getDetailedScore() + " avg: " + avgScore + " worst: " + population.get(population.size() - 1).getDetailedScore()));
        LOG.info("");

        // pick 20 of the best
        List<PlayingAgent> breedPool = new ArrayList<>();
        Iterator<PlayingAgent> iterator = population.iterator();
        int pickCnt = 100;

        while (pickCnt > 0 && iterator.hasNext()) {
            PlayingAgent picked = iterator.next();
//            System.out.println(picked.getName() + ": " + picked.getScore());
            iterator.remove();
            breedPool.add(picked);
            pickCnt--;
        }

//        if (avgScore == oldAvgScore) {
//            return;
//        }
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

        start();
    }

    private Player play(Board board, PlayingAgent playingAgent, PlayingAgent opponent) {
        board.reset();
        List<Coordinate> freeCells = board.freeCells();
        while (!freeCells.isEmpty()) {
            Coordinate move = playingAgent.play(board.activePlayer(), board);
            boolean valid = board.set(move);
            if (!valid) {
                playingAgent.feedback(GameResult.LOST, -2);
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
                opponent.feedback(GameResult.LOST, -2);
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
