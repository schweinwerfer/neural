package de.ora.neural.core.net;

import java.util.Random;

public class Vector {
    double[] data;
    int length;

    public Vector(double... data) {
        this.data = data;
        this.length = data.length;
    }

    public Vector(int length) {
        this.length = length;
        this.data = new double[length];
    }

    public Vector initRandom() {
        Random rnd = new Random();
        final double[] doubles = rnd.doubles(length, -1, 1.0001).toArray();
        for (int i = 0; i < length; i++) {
            data[i] = doubles[i];
        }

        return this;
    }

    public Vector add(Vector input) {
        if (length != input.length) {
            throw new IllegalArgumentException("Invalid input dimensions: expected " + length + " was" + input.length);
        }

        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = data[i] + input.data[i];
        }

        return new Vector(result);
    }

    public Vector subtract(final Vector input) {
        if (length != input.length) {
            throw new IllegalArgumentException("Invalid input dimensions: expected " + length + " was" + input.length);
        }

        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = data[i] - input.data[i];
        }

        return new Vector(result);
    }

    public Vector pow(double exponent) {
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = Math.pow(data[i], exponent);
        }

        return new Vector(result);
    }

    public double sumElements() {
        double result = 0;
        for (int i = 0; i < length; i++) {
            result += data[i];
        }

        return result;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(data[i]).append(System.lineSeparator());
        }
        return sb.toString();
    }


    public Vector initWith(double value) {
        for (int i = 0; i < length; i++) {
            data[i] = value;
        }

        return this;
    }

    public Vector multiply(double value) {
        for (int i = 0; i < length; i++) {
            data[i] = data[i] * value;
        }

        return this;
    }

    public void applyOnEachElement(final VectorElementFunction function) {
        for (int i = 0; i < length; i++) {
                data[i] = function.transform(data[i], i);
        }
    }
}
