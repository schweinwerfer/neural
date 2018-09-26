package de.ora.neural.core.net;

import java.util.Random;

public class Matrix {
    double[][] data;
    int rows;
    int columns;

    public Matrix(double[][] data) {
        this.data = data;
        this.rows = data.length;
        this.columns = data[0].length;
    }

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    public Matrix copy() {
        return new Matrix(data);
    }

    public Matrix initOnes() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = 1;
            }
        }

        return this;
    }

    public Matrix initRandom() {
        Random rnd = new Random();
        for (int i = 0; i < rows; i++) {
            final double[] doubles = rnd.doubles(columns, -1, 1.0001).toArray();
            for (int j = 0; j < columns; j++)
                data[i][j] = doubles[j];
        }

        return this;
    }

    /**
     * Multiply each element with a scalar
     *
     * @param input
     */
    public void multiply(double input) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = data[i][j] * input;
            }
        }
    }

    public Vector multiply(Vector input) {
        if (input.getLength() != columns) {
            throw new IllegalArgumentException("Invalid input dimensions: expected " + columns + " was" + input.getLength());
        }

        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            double sum = 0;
            for (int j = 0; j < columns; j++) {
                sum += data[i][j] * input.data[j];
            }
            result[i] = sum;
        }

        return new Vector(result);
    }

    /**
     * Applies the exponential function element wise.
     */
    public void exp() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = Math.exp(data[i][j]);
            }
        }
    }

    /**
     * Adds a scalar to each element
     *
     * @param input
     */
    public void add(double input) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = data[i][j] + input;
            }
        }
    }

    /**
     * Divides element wise this matrix by the input
     *
     * @param input
     * @return
     */
    public Matrix divide(Matrix input) {
        if (!dimensionsMatch(input)) {
            throw new IllegalArgumentException("Dimensions do not match: expected rows " + rows + " found " + input.rows + ", expected columns " + columns + " found " + input.columns);
        }
        Matrix result = new Matrix(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = data[i][j] / input.data[i][j];
            }
        }

        return result;
    }

    public void applyOnEachElement(final MatrixElementFunction function) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = function.transform(data[i][j]);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    private boolean dimensionsMatch(Matrix input) {
        return rows == input.rows && columns == input.columns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(data[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
