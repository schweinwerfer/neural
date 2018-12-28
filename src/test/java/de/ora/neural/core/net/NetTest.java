package de.ora.neural.core.net;

import de.ora.neural.core.activation.SigmoidActivationFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NetTest {


    private Random rnd = new Random();

    @Test
    public void propagate() {

        Net net = new Net(0.15)
                .addHiddenLayer(new HiddenLayer(4, 3, new SigmoidActivationFunction()))
                .addHiddenLayer(new HiddenLayer(3, 2, new SigmoidActivationFunction()))
                .addOutputLayer(new OutputLayer(2, 1, new SigmoidActivationFunction()));

        List<TrainingData> trainingData = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Vector randomInput = randomInput(4);
            double expected = xor(randomInput);
            trainingData.add(new TrainingData(randomInput, new Vector(expected)));
        }

        double error = 1;

        while (error > 0.001) {
            error = net.train(trainingData);
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
