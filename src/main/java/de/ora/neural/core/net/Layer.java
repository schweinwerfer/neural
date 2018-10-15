package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class Layer {
    private int inputLength;
    private int outputLength;
    private Vector input; // last input from the previous layer
    private Matrix weights;
    private Vector biases;
    private Vector weightedInput; // z = Wa+b of this layer before application of applyActivation function
    private Vector output; // applyActivation result a = s(Wa+b)
    private ActivationFunction activationFunction;


    public Layer(int inputLength, int size, ActivationFunction activationFunction) {
        input = new Vector(inputLength);
        weights = new Matrix(size, inputLength).initRandom();
        biases = new Vector(size).initRandom();
        this.inputLength = inputLength;
        this.outputLength = size;
        this.activationFunction = activationFunction;
    }


    /**
     * Propagates an input Vector through this layer resulting in an output layer.
     *
     * @param input
     * @return
     */
    public Vector propagate(Vector input) {
        if (inputLength != input.length) {
            throw new IllegalArgumentException("Invalid input dimensions: expected " + inputLength + " was " + input.length);
        }
        this.input = input;
        this.weightedInput = weights.multiply(input).add(biases);
        this.output = applyActivation(weightedInput);
        return this.output;
    }

    /**
     * Calcs cost of this layer for one concrete input
     */
    public double calcCost(final Vector expected) {
        if (outputLength != expected.length) {
            throw new IllegalArgumentException("Invalid output dimensions: expected " + outputLength + " was " + expected.length);
        }

        double cost = 0;
        for (int i = 0; i < outputLength; i++) {
            cost += Math.pow(output.data[i] - expected.data[i], 2);
        }

        return cost * 0.5;
    }

    public boolean isCompatibleTo(final Layer previous) {
        return previous.outputLength == this.inputLength;
    }

    /**
     * Applies the given activation function on the input
     */
    private Vector applyActivation(final Vector input) {
        Vector result = new Vector(input.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = activationFunction.apply(input.data[i]);
        }

        return result;
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
