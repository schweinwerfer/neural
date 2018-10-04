package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class Layer {
    private int inputLength;
    private int outputLength;
    private Vector input; // last input from the previous layer
    private Matrix weights;
    private Vector biases;
    private Vector rawOutput; // output z = Wa+b of this layer before application of activation function
    private Vector output; // activation result a = s(Wa+b)
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
        this.rawOutput = weights.multiply(input).add(biases);
        this.output = activation(rawOutput);
        return this.output;
    }

    public boolean isCompatibleTo(final Layer previous) {
        return previous.outputLength == this.inputLength;
    }

    private Vector activation(final Vector input) {
        Vector result = new Vector(input.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = activationFunction.apply(input.data[i]);
        }

        return input;
    }

    private Vector derivateCostForBiases(final Vector expected) {
        Vector result = new Vector(biases.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = activationFunction.derivate(rawOutput.data[i]) * 2 * (output.data[i] - expected.data[i]);
        }

        return result;
    }

    private Vector derivateCostForWeights(final Vector expected) {
        Vector result = new Vector(biases.length);
        for (int i = 0; i < result.length; i++) {
            result.data[i] = input.data[i] * activationFunction.derivate(rawOutput.data[i]) * 2 * (output.data[i] - expected.data[i]);
        }

        return result;
    }

    private Matrix derivateCostForInput(final Vector expected) {
        Matrix result = new Matrix(weights.getRows(), weights.getColumns());
        for (int i = 0; i < weights.rows; i++) {
            for (int j = 0; j < weights.columns; j++) {
                result.data[i][j] = weights.data[i][j] * activationFunction.derivate(rawOutput.data[i]) * 2 * (output.data[i] - expected.data[i]);
            }

        }

        return result;
    }


}
