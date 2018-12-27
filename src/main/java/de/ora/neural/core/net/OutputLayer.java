package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class OutputLayer extends Layer {

    public OutputLayer(int inputLength, int size, ActivationFunction activationFunction) {
        super(inputLength, size, activationFunction);
    }

    /**
     * Calcs output error of the output layer for one concrete input
     */
    public double error(final Vector expected) {
        if (outputLength != expected.length) {
            throw new IllegalArgumentException("Invalid output dimensions: expected " + outputLength + " was " + expected.length);
        }

        double cost = 0;
        for (int i = 0; i < outputLength; i++) {
            cost += Math.pow(output.data[i] - expected.data[i], 2);
        }

        return cost * 0.5;
    }

    /**
     * Only applicable for the output layer:
     * Calculate the error of each neuron in this layer
     * δL = (aL−y) ⊙ σ′(zL)  [BP1]
     */
    private Vector derivateCostForWeights(final Vector expected) {
        Vector result = new Vector(biases.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = (output.data[i] - expected.data[i]) * activationFunction.derivate(weightedInput.data[i]);
        }

        return result;
    }
}
