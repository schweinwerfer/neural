package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public abstract class Layer {
    protected int inputLength;
    protected int neurons;
    @JsonIgnore
    protected Vector layerErrorSignal; // for each neuron: (activation function gradient) * (error of output)
    @JsonIgnore
    private Vector input; // last input from the previous layer
    private Matrix weights; // W
    protected Vector biases; // b
    @JsonIgnore
    protected Vector weightedInput; // z = Wa+b of this layer before application of applyActivation function
    @JsonIgnore
    protected Vector output; // applyActivation result a = s(Wa+b)

    @JsonSerialize(using = ActivationFunctionSerializer.class)
    @JsonDeserialize(using = ActivationFunctionDeserializer.class)
    protected ActivationFunction activationFunction;

    protected Layer() {
    }

    protected Layer(int inputLength, int neurons, ActivationFunction activationFunction) {
        input = new Vector(inputLength);
        weights = new Matrix(neurons, inputLength).initRandom();
        biases = new Vector(neurons).initRandom();
        this.inputLength = inputLength;
        this.neurons = neurons;
        this.activationFunction = activationFunction;
    }

    public void setTestMode() {
        weights = new Matrix(neurons, inputLength).initOnes();
        biases = new Vector(neurons).initWith(1.0);
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
    public Vector calcInputErrors(final Vector layerErrorSignals) {
        Vector result = new Vector(inputLength);
        for (int i = 0; i < inputLength; i++) {

            Vector incomingWeights = weights.getColumn(i); // get all incoming weights to current node
            double inputError = 0;
            for (int j = 0; j < incomingWeights.length; j++) {
                inputError += incomingWeights.data[j] * layerErrorSignals.data[j];
            }

            result.data[i] = inputError;
        }

        return result;
    }

    public void adapt(double learningRate) {
        weights.applyOnEachElement(new MatrixElementFunction() {
            @Override
            public double transform(double value, int neuron, int column) {
                double adaptation = (-1) * learningRate * layerErrorSignal.data[neuron] * input.data[column];
                return value + adaptation;
            }
        });

        biases.applyOnEachElement(new VectorElementFunction() {
            @Override
            public double transform(double data, int neuron) {
                double adaptation = (-1) * learningRate * layerErrorSignal.data[neuron];
                return data + adaptation;
            }
        });
    }

    public void cleanup() {
        this.input = null;
        this.weightedInput = null;
        this.layerErrorSignal = null;
    }

    public int getInputLength() {
        return inputLength;
    }

    public int getNeurons() {
        return neurons;
    }

    public Matrix getWeights() {
        return weights;
    }

    public Vector getBiases() {
        return biases;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }
}
