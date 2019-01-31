package de.ora.tictactoe.genetic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public abstract class ATournament {
    protected static final Logger LOG = Logger.getLogger(TrainerTournament.class.getSimpleName());
    protected static final int POPULATION_SIZE = 1000;
    private File agentDir;
    protected List<PlayingAgent> population = new ArrayList<>();
    protected double avgFitness;
    protected Random rnd;
    int epoch = 0;

    public ATournament(File agentDir) {
        rnd = new Random();
        this.agentDir = agentDir;
    }

    protected void doAfterTournament(String s) throws IOException {
        File agentDir2 = new File(agentDir, s);
        agentDir2.mkdirs();
        for (int i = 0; i < 20; i++) {
            PlayingAgent agent = population.get(i);
            agent.store(agentDir2);
        }
    }

    protected void breed(List<PlayingAgent> breedPool) {
        // breed
        int lastWinner = -1;
        for (int i = 0; i < breedPool.size(); i++) {
            for (int j = 0; j < breedPool.size(); j++) {
                if (population.size() >= POPULATION_SIZE) {
                    break;
                }

                int breed = rnd.nextInt(100);
                if (i != j && breed <= 45) {
                    population.add(breedPool.get(i).combine(breedPool.get(j)));
                }

                if (breed > 95) {
                    population.add(breedPool.get(i).mutate());
                }

                if (lastWinner != i && i < 100) {
                    population.add(breedPool.get(i)); // add old winners
                    lastWinner = i;
                }
            }
        }
    }

    protected List<PlayingAgent> pickBest() {
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
        return breedPool;
    }

    protected void createPopulation(int size) {
        for (int i = 0; i < size; i++) {
            population.add(new PlayingAgent("a" + i));
        }
    }

    protected void fillPopulation(int size) {
        int diff = population.size() - size;
        if (diff >= 0) {
            return;
        }
        diff = Math.abs(diff);

        for (int i = 0; i < diff; i++) {
            population.add(new PlayingAgent("a" + i));
        }
    }

    protected void loadPopulation() throws IOException {
        File[] files = this.agentDir.listFiles();
        for (File file : files) {
            population.add(PlayingAgent.load(file, PlayingAgent.class));
        }
    }
}
