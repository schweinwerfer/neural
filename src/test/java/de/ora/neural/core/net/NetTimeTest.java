package de.ora.neural.core.net;

import de.ora.neural.util.Stopwatch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NetTimeTest {


    private Random rnd = new Random();

    @Test
    public void propagate() throws IOException {
        File netFile = new File("net-xor.json");
        Net net = Net.load(netFile);
        if (net == null) {
            net = new Net(2, 2, 1);
            net.store(netFile);
        }

        List<TrainingData> trainingSet = new ArrayList<>();
        trainingSet.add(new TrainingData(new Vector(0, 0), new Vector(0.0)));
        trainingSet.add(new TrainingData(new Vector(0, 1), new Vector(1.0)));
        trainingSet.add(new TrainingData(new Vector(1, 0), new Vector(1.0)));
        trainingSet.add(new TrainingData(new Vector(1, 1), new Vector(0.0)));

        double error = 1;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (error > 0.01) {
            if (net.getEpoch() > 200000) {
                netFile.delete();
                break;
            }
            error = net.train(trainingSet);
        }

        System.out.println("Done in " + stopwatch.stop());
        System.out.println("0,0 -> " + net.propagate(new Vector(0, 0)));
        System.out.println("1,0 -> " + net.propagate(new Vector(1, 0)));
        System.out.println("0,1 -> " + net.propagate(new Vector(0, 1)));
        System.out.println("1,1 -> " + net.propagate(new Vector(1, 1)));
    }
}
