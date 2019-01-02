package de.ora.neural.core.net;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Net {
    private static final Logger LOG = Logger.getLogger(Net.class.getSimpleName());
    public double learningRate = 0.05;
    Deque<HiddenLayer> layers = new LinkedList<>();
    private OutputLayer outputLayer;
    private double avgError;
    private int trainingEpoch = 0;

    public Net(double learningRate) {
        this.learningRate = learningRate;
    }

    public Net addHiddenLayer(final HiddenLayer layer) {
        if (!layers.isEmpty() && !layer.isCompatibleTo(layers.getLast())) {
            throw new IllegalArgumentException("Provided layer is not compatible to previous layer!");
        }

        layers.add(layer);

        return this;
    }

    public Net addOutputLayer(final OutputLayer layer) {
        if (!layers.isEmpty() && !layer.isCompatibleTo(layers.getLast())) {
            throw new IllegalArgumentException("Provided layer is not compatible to previous layer!");
        }

        outputLayer = layer;

        return this;
    }

    public Vector propagate(final Vector input) {
        final Iterator<HiddenLayer> iterator = layers.iterator();

        Vector nextInput = input;

        HiddenLayer next;
        while (iterator.hasNext()) {
            next = iterator.next();
            nextInput = next.propagate(nextInput);
        }

        nextInput = outputLayer.propagate(nextInput);

        return nextInput;
    }

    public double train(final List<TrainingData> trainingData) {
        cleanupOldTrainingData();

        Vector quadraticError = null;

        for (TrainingData trainingDatum : trainingData) {
            Vector error = train(trainingDatum.getInput(), trainingDatum.getExpectedOutput());
            if (quadraticError == null) {
                quadraticError = error;
            } else {
                quadraticError = quadraticError.add(error);
            }

        }

        trainingEpoch++;

        quadraticError = quadraticError.multiply(0.5);

        this.avgError = 0;
        for (double datum : quadraticError.data) {
            this.avgError += datum;
        }
        this.avgError = this.avgError / quadraticError.length;
        LOG.info(String.format("Current avg quadraticError %.10f for epoch %s", this.avgError, trainingEpoch));
        return this.avgError;
    }

    private void cleanupOldTrainingData() {
        for (HiddenLayer layer : layers) {
            layer.cleanup();
        }
        this.outputLayer.cleanup();
    }

    Vector train(final Vector input, final Vector expectedOutput) {
        Vector result = propagate(input);
        Vector error = outputLayer.error(expectedOutput);
        Vector errorSignificance = outputLayer.calcErrorSignificance(expectedOutput);

        Vector inputErrors = outputLayer.calcInputErrors(errorSignificance);
        Iterator<HiddenLayer> iterator = layers.descendingIterator();
        HiddenLayer next;
        while (iterator.hasNext()) {
            next = iterator.next();
            errorSignificance = next.calcErrorSignificance(inputErrors);
            inputErrors = next.calcInputErrors(errorSignificance);
        }

        for (HiddenLayer layer : layers) {
            layer.adapt(learningRate);
        }

        outputLayer.adapt(learningRate);

        return error;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public int getTrainingEpoch() {
        return trainingEpoch;
    }
}
