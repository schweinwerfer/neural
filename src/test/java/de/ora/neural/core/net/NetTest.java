package de.ora.neural.core.net;

import de.ora.neural.core.activation.SigmoidActivationFunction;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class NetTest {


    @Test
    public void propagate() {

        Net net = new Net(0.05)
                .addHiddenLayer(new HiddenLayer(4, 3, new SigmoidActivationFunction()))
                .addHiddenLayer(new HiddenLayer(3, 2, new SigmoidActivationFunction()))
                .addOutputLayer(new OutputLayer(2, 1, new SigmoidActivationFunction()));

        Map<Vector, Vector> trainingSet = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Vector randomInput = randomInput(4);
            double expected = xor(randomInput);
            trainingSet.put(randomInput, new Vector(expected));
        }

        double error = 1;

        while (error > 0.0001) {
            for (Map.Entry<Vector, Vector> entry : trainingSet.entrySet()) {
                error = net.train(entry.getKey(), entry.getValue());
            }
        }
    }

    private Vector randomInput(int size) {
        Random rnd = new Random();
        Vector result = new Vector(size);

        for (int i = 0; i < size; i++) {
            result.data[i] = rnd.nextBoolean() ? 1 : 0;
        }

        return result;
    }

    private double xor(Vector input) {
        Boolean result = null;
        for (double value : input.data) {
            if (result == null) {
                result = Boolean.getBoolean(value + "");
            }
            result ^= Boolean.getBoolean(value + "");
        }

        return result ? 1 : 0;
    }

}
