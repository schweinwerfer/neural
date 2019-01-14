package de.ora.tictactoe.genetic;

import de.ora.tictactoe.*;
import de.ora.util.Stopwatch;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TrainerTournament extends ATournament {

    public TrainerTournament() {
        super(new File("tournament/trainer/"));
    }

    public static void main(String[] args) throws IOException {
        TrainerTournament tournament = new TrainerTournament();
        tournament.createPopulation(POPULATION_SIZE);
        tournament.start();
    }

    private void start() throws IOException {
        boolean isRunning = true;

        ThreeXThreeBoard board = new ThreeXThreeBoard();
        Trainer trainer = new Trainer(board);
        double bestScore = 0;

        Set<String> boardConfigs = new HashSet<>();
        Set<String> playConfigs = new HashSet<>();

        Set<Map.Entry<String, List<Coordinate>>> entries = trainer.getMoves().entrySet();

        for (Map.Entry<String, List<Coordinate>> entry : entries) {
            List<Coordinate> moves = entry.getValue();
            if (moves.isEmpty() || moves.size() == 1) {
                continue; // nicht interessant
            }

            if (moves.size() == 2) {
                // interessant für ein spiel
                playConfigs.add(entry.getKey());
                continue;
            }

            boardConfigs.add(entry.getKey());
        }

        while (isRunning) {
            int gamesPlayed = 0;
            LOG.info("Tournament " + (++epoch) + " size: " + population.size());
            Stopwatch stopwatch = Stopwatch.createStarted();

            for (PlayingAgent agent : population) {

                for (String boardConfig : boardConfigs) {
                    Board config = Board.fromKey(boardConfig);
                    Player player = config.activePlayer();
                    Coordinate move = agent.play(player, config);
                    config.set(move);
                    Player winner = config.findWinner();
                    switch (winner) {

                        case NONE:
                            if (config.freeCells().isEmpty()) {
                                agent.feedback(GameResult.DRAW);
                            }
                            break;

                        default:
                            agent.feedback(winner == player ? GameResult.WON : GameResult.LOST);
                            break;
                    }
                    gamesPlayed++;
                }

                for (String playConfig : playConfigs) {
                    Board config = Board.fromKey(playConfig);
                    Player winner = play(board, trainer, agent);
                    notifyPlayers(winner, trainer, agent);
                    gamesPlayed++;

                    config = Board.fromKey(playConfig);
                    winner = play(board, agent, trainer);
                    notifyPlayers(winner, agent, trainer);
                    gamesPlayed++;
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
            double fitness = bestAgent.getFitness();
            if (fitness > bestScore) {
                bestScore = fitness;
                if (bestScore > 0.99) {
                    doAfterTournament("tournament/trainer/tmp/" + fitness);
                }
            }

            if (fitness == 1.0) {
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
        doAfterTournament("tournament/trainer/ng2/");

    }

    private void notifyPlayers(Player winner, PlayingAgent player1, PlayingAgent player2) {
        switch (winner) {
            case PLAYER1:
                player1.feedback(GameResult.WON);
                player2.feedback(GameResult.LOST);
                break;
            case PLAYER2:
                player1.feedback(GameResult.LOST);
                player2.feedback(GameResult.WON);
                break;
            case NONE:
                player1.feedback(GameResult.DRAW);
                player2.feedback(GameResult.DRAW);
                break;
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


}
