package de.ora.neural.core.net;

import de.ora.neural.core.activation.IdentityActivationFunction;
import de.ora.neural.core.activation.SigmoidActivationFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NetTestDetail {


    private Random rnd = new Random();

    @Test
    public void propagate() {

        Net net = new Net(0.01)
                .addHiddenLayer(new HiddenLayer(2, 3, new IdentityActivationFunction()))
                .addOutputLayer(new OutputLayer(3, 1, new IdentityActivationFunction()));

        List<TrainingData> trainingSet = new ArrayList<>();
        trainingSet.add(new TrainingData(new Vector(0, 0), new Vector(0.0)));
        trainingSet.add(new TrainingData(new Vector(0, 1), new Vector(1.0)));
        trainingSet.add(new TrainingData(new Vector(1, 0), new Vector(1.0)));
        trainingSet.add(new TrainingData(new Vector(1, 1), new Vector(0.0)));

        Vector result = net.propagate(new Vector(0, 1));
        Vector error = net.train(new Vector(0, 1), new Vector(1.0));
        result = net.propagate(new Vector(0, 1));

    }
}
