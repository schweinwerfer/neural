package de.ora.neural.core.net;

import de.ora.neural.core.activation.SigmoidActivationFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NetTestSimple {


    private Random rnd = new Random();

    @Test
    public void propagate() {

        Net net = new Net(0.01)
                .addHiddenLayer(new HiddenLayer(2, 3, new SigmoidActivationFunction()))
                .addHiddenLayer(new HiddenLayer(3, 3, new SigmoidActivationFunction()))
                .addOutputLayer(new OutputLayer(3, 1, new SigmoidActivationFunction()));

        List<TrainingData> trainingData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Vector randomInput = randomInput(2);
            double expected = xor(randomInput);
            trainingData.add(new TrainingData(randomInput, new Vector(expected)));
        }

        double error = 1;

        while (error > 0.2) {
            error = net.train(trainingData);
        }

        for (TrainingData trainingDatum : trainingData) {
            System.out.println("> " + trainingDatum.getInput() + " -> " + net.propagate(trainingDatum.getInput()));
        }
    }

    private Vector randomInput(int size) {

        Vector result = new Vector(size);

        for (int i = 0; i < size; i++) {
            result.data[i] = rnd.nextBoolean() ? 1 : 0;
        }

        return result;
    }

    private double xor(Vector input) {
        int oneCnt = 0;
        for (Double value : input.data) {
            boolean boolValue = value.intValue() != 0;
            if (boolValue) {
                oneCnt++;
            }
        }

        return oneCnt == 1 ? 1.0 : 0.0;
    }

}
