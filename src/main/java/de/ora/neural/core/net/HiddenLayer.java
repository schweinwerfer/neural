package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class HiddenLayer extends Layer {

    public HiddenLayer(int inputLength, int neurons, ActivationFunction activationFunction) {
        super(inputLength, neurons, activationFunction);
    }

    /**
     * Calculates the intensity of error of each neuron of this layer.
     */
    public Vector calcErrorSignificance(final Vector layerOutputErrors) {
        Vector result = new Vector(neurons);
        for (int i = 0; i < result.length; i++) {
            double gradient = activationFunction.derivate(weightedInput.data[i]);
            double nodeOutputErrors = layerOutputErrors.data[i];
            result.data[i] = nodeOutputErrors * gradient;
        }

        this.errorSignificances = result;

        return result;
    }
}
