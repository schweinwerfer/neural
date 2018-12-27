package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class OutputLayer extends Layer {

    public OutputLayer(int inputLength, int neurons, ActivationFunction activationFunction) {
        super(inputLength, neurons, activationFunction);
    }

    /**
     * Calcs output error of the output layer for one concrete input
     */
    public double error(final Vector expected) {
        if (neurons != expected.length) {
            throw new IllegalArgumentException("Invalid output dimensions: expected " + neurons + " was " + expected.length);
        }

        double cost = 0;
        for (int i = 0; i < neurons; i++) {
            cost += Math.pow(output.data[i] - expected.data[i], 2);
        }

        return cost * 0.5;
    }

    /**
     * Calculates the intensity of error of each neuron of this layer.
     */
    public Vector calcErrorSignificance(final Vector expected) {
        Vector result = new Vector(neurons);
        for (int i = 0; i < result.length; i++) {
            double gradient = activationFunction.derivate(weightedInput.data[i]);
            double error = output.data[i] - expected.data[i];
            result.data[i] = error * gradient;
        }

        this.errorSignificances = result;
        return result;
    }


}
