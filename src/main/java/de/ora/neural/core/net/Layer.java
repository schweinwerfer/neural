package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class Layer {
    private int inputLength;
    private Vector input; // activations
    private Matrix weights;
    private Vector biases;
    private Vector output; // sigmoid result = s(Wa+b)
    private ActivationFunction activationFunction;

    public Layer(int inputLength, int outputLength, ActivationFunction activationFunction) {
        input = new Vector(inputLength);
        weights = new Matrix(inputLength, outputLength).initRandom();
        biases = new Vector(outputLength).initRandom();
        this.inputLength = inputLength;
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
            throw new IllegalArgumentException("Invalid input dimensions: expected " + inputLength + " was" + input.length);
        }
        this.input = input;
        this.output = sigmoid(weights.multiply(input).add(biases));
        return this.output;
    }

    private Vector sigmoid(Vector input) {
        Vector result = new Vector(input.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = activationFunction.apply(input.data[i]);
        }

        return input;
    }
}
