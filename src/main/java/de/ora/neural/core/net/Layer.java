package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class Layer {
    private int inputLength;
    private int outputLength;
    private Vector input; // activations
    private Matrix weights;
    private Vector biases;
    private Vector output; // activation result = s(Wa+b)
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
        this.output = activation(weights.multiply(input).add(biases));
        return this.output;
    }

    public boolean isCompatibleTo(final Layer previous) {
        return previous.outputLength == this.inputLength;
    }

    private Vector activation(Vector input) {
        Vector result = new Vector(input.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = activationFunction.apply(input.data[i]);
        }

        return input;
    }
}
