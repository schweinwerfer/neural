package de.ora.neural.core.net;

public class TrainingData {
    private Vector input;
    private Vector expectedOutput;

    public TrainingData(Vector input, Vector expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public Vector getInput() {
        return input;
    }

    public Vector getExpectedOutput() {
        return expectedOutput;
    }
}
