package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class Layer {
    protected int inputLength;
    protected int neurons;
    protected Vector errorSignificances; // for each neuron: (activation function gradient) * (error of output)
    private Vector input; // last input from the previous layer
    private Matrix weights;
    protected Vector biases;
    protected Vector weightedInput; // z = Wa+b of this layer before application of applyActivation function
    protected Vector output; // applyActivation result a = s(Wa+b)
    protected ActivationFunction activationFunction;


    public Layer(int inputLength, int neurons, ActivationFunction activationFunction) {
        input = new Vector(inputLength);
        weights = new Matrix(neurons, inputLength).initRandom();
        biases = new Vector(neurons).initRandom();
        this.inputLength = inputLength;
        this.neurons = neurons;
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
        return previous.neurons == this.inputLength;
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
     * Get the output error sum for each previous neuron (=input for this neuron)
     */
    public Vector calcInputErrors(final Vector errorSignificances) {
        Vector result = new Vector(inputLength);
        for (int i = 0; i < inputLength; i++) {

            Vector incomingWeights = weights.getColumn(i); // get all incoming weights to current node
            double inputError = 0;
            for (int j = 0; j < incomingWeights.length; j++) {
                inputError += incomingWeights.data[j] * errorSignificances.data[j];
            }

            result.data[i] = inputError;
        }

        return result;
    }

    public void adapt(double learningRate) {
        weights.applyOnEachElement(new MatrixElementFunction() {
            @Override
            public double transform(double input, int row, int column) {
                double adaptation = (-1) * learningRate * output.data[row] * errorSignificances.data[row];
                return adaptation + input;
            }
        });
    }

}
