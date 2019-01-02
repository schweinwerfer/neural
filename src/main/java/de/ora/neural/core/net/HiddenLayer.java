package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;

public class HiddenLayer extends Layer {

    public HiddenLayer(int inputLength, int neurons, ActivationFunction activationFunction) {
        super(inputLength, neurons, activationFunction);
    }

    /**
     * Calculates the intensity of error of each neuron of this layer.
     */
    public Vector calcLayerErrorSignal(final Vector layerOutputErrors) {
        Vector result = new Vector(neurons);
        for (int i = 0; i < result.length; i++) {
            double gradient1 = output.data[i] * (1 - output.data[i]);
//            double gradient = activationFunction.derivate(weightedInput.data[i]);
            double nodeOutputErrors = layerOutputErrors.data[i];
            result.data[i] = nodeOutputErrors * gradient1;
        }

        this.layerErrorSignal = result;

        return result;
    }
}
