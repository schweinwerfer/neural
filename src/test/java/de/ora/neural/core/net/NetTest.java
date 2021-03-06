package de.ora.neural.core.net;

import de.ora.neural.core.activation.SigmoidActivationFunction;
import de.ora.util.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NetTest {


    private Random rnd = new Random();

    @Test
    public void propagate() {

        Net net = new Net(0.01)
                .addHiddenLayer(new HiddenLayer(2, 2, new SigmoidActivationFunction()))
//                .addHiddenLayer(new HiddenLayer(2, 3, new SigmoidActivationFunction()))
                .addOutputLayer(new OutputLayer(2, 1, new SigmoidActivationFunction()));

        List<TrainingData> trainingSet = new ArrayList<>();
        trainingSet.add(new TrainingData(new Vector(0, 0), new Vector(0.0)));
        trainingSet.add(new TrainingData(new Vector(0, 1), new Vector(1.0)));
        trainingSet.add(new TrainingData(new Vector(1, 0), new Vector(1.0)));
        trainingSet.add(new TrainingData(new Vector(1, 1), new Vector(0.0)));

        double error = 1;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (error > 0.01) {
            error = net.train(trainingSet);
        }

        System.out.println("Done in " + stopwatch.stop());
        System.out.println("0,0 -> " + net.propagate(new Vector(0, 0)));
        System.out.println("1,0 -> " + net.propagate(new Vector(1, 0)));
        System.out.println("0,1 -> " + net.propagate(new Vector(0, 1)));
        System.out.println("1,1 -> " + net.propagate(new Vector(1, 1)));
    }
}
