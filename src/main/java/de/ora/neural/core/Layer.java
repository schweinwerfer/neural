package de.ora.neural.core;

public class Layer {
    private int inputLength;
    private Vector input; // activations
    private Matrix weights;
    private Vector biases;
    private Vector output; // sigmoid result = s(Wa+b)

    public Layer(int inputLength, int outputLength) {
        input = new Vector(inputLength);
        weights = new Matrix(inputLength, outputLength);
        biases = new Vector(outputLength);
        this.inputLength = inputLength;
    }

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
            result.data[i] = Functions.rectifiedLU(input.data[i]);
        }

        return input;
    }
}
