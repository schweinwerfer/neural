package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class OutputLayer extends Layer {

    public OutputLayer(int inputLength, int neurons, ActivationFunction activationFunction) {
        super(inputLength, neurons, activationFunction);
    }

    /**
     * Calcs output error of the output layer for one concrete input
     */
    public Vector error(final Vector expected) {
        if (neurons != expected.length) {
            throw new IllegalArgumentException("Invalid output dimensions: expected " + neurons + " was " + expected.length);
        }

        Vector result = new Vector(this.output.length);

        return expected.subtract(this.output).pow(2);
    }

    /**
     * Calculates the intensity of error of each neuron of this layer.
     */
    public Vector calcLayerErrorSignal(final Vector expected) {
        Vector result = new Vector(neurons);
        for (int i = 0; i < result.length; i++) {
//            double gradient1 = output.data[i] * (1 - output.data[i]);
            double gradient = activationFunction.derivate(weightedInput.data[i]);
            double error = output.data[i] - expected.data[i];
            result.data[i] = error * gradient;
        }

        this.layerErrorSignal = result;
        return result;
    }


}
