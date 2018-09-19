package de.ora.neural.core;

public class Vector {
    double[] data;
    int length;

    public Vector(double[] data) {
        this.data = data;
        this.length = data.length;
    }

    public Vector(int length) {
        this.length = length;
        this.data = new double[length];
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

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(data[i]).append("\n");
        }
        return sb.toString();
    }
}
