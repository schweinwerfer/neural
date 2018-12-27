package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class Layer {
    private int inputLength;
    protected int outputLength;
    private Vector input; // last input from the previous layer
    private Matrix weights;
    protected Vector biases;
    protected Vector weightedInput; // z = Wa+b of this layer before application of applyActivation function
    protected Vector output; // applyActivation result a = s(Wa+b)
    protected ActivationFunction activationFunction;


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





}
