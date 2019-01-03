package de.ora.neural.core.net;

import de.ora.neural.core.activation.SigmoidActivationFunction;
import de.ora.neural.util.Stopwatch;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Net {
    @JsonIgnore
    private static final Logger LOG = Logger.getLogger(Net.class.getSimpleName());
    public double learningRate = 0.01;
    Deque<HiddenLayer> layers = new LinkedList<>();
    private OutputLayer outputLayer;
    private double avgError;
    private int epoch = 0;

    private Net() {
    }

    public Net(int... dims) {
        int noOfLayers = dims.length;
        int i = 0;
        while (noOfLayers > 2) {
            layers.add(new HiddenLayer(dims[i], dims[++i], new SigmoidActivationFunction()));
            noOfLayers--;
        }
        outputLayer = new OutputLayer(dims[i], dims[++i], new SigmoidActivationFunction());
    }

    public Net(double learningRate) {
        this.learningRate = learningRate;
    }


    public double train(final List<TrainingData> trainingData) {
        Stopwatch stopwatch = Stopwatch.createStarted();
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

        epoch++;

        quadraticError = quadraticError.multiply(0.5);

        this.avgError = 0;
        for (double datum : quadraticError.data) {
            this.avgError += datum;
        }
        this.avgError = this.avgError / quadraticError.length;

        LOG.info(String.format("Current avg quadraticError %.10f for epoch %s duration %s", this.avgError, epoch, stopwatch.stop().toString()));
        return this.avgError;
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


    public Vector train(final Vector input, final Vector expectedOutput) {
        Vector result = propagate(input);
        Vector error = outputLayer.error(expectedOutput);
        Vector layerErrorSignal = outputLayer.calcLayerErrorSignal(expectedOutput);
        Vector inputErrors = outputLayer.calcInputErrors(layerErrorSignal);

        Iterator<HiddenLayer> iterator = layers.descendingIterator();
        HiddenLayer next;
        while (iterator.hasNext()) {
            next = iterator.next();
            layerErrorSignal = next.calcLayerErrorSignal(inputErrors);
            inputErrors = next.calcInputErrors(layerErrorSignal);
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

    public int getEpoch() {
        return epoch;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public Deque<HiddenLayer> getLayers() {
        return layers;
    }

    public OutputLayer getOutputLayer() {
        return outputLayer;
    }

    public double getAvgError() {
        return avgError;
    }

    public File store(final File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, this);

        return file;
    }

    public File store(final String filename) throws IOException {
        File file = new File(filename + ".json");
        return store(file);
    }

    public static Net load(final File file) throws IOException {
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Net.class);
    }

    public static Net load(final String filename) throws IOException {
        File file = new File(filename + ".json");
        return load(file);
    }

    private void cleanupOldTrainingData() {
        for (HiddenLayer layer : layers) {
            layer.cleanup();
        }
        this.outputLayer.cleanup();
    }
}
