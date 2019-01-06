package de.ora.neural.core.net;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Matrix {
    public double[][] data;
    int rows;
    int columns;

    private Matrix() {
    }

    public Matrix(double[][] otherData) {
        this.rows = otherData.length;
        this.columns = otherData[0].length;
        this.data = new double[this.rows][this.columns];
        for (int row = 0; row < otherData.length; row++) {
            for (int cell = 0; cell < otherData[row].length; cell++) {
                this.data[row][cell] = otherData[row][cell];
            }
        }
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
        return initWith(1);
    }

    public Matrix initWith(double value) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = value;
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
     * Elementwise multiplication
     *
     * @param input
     * @return
     */
    public Matrix elemMultiply(final Matrix input) {
        assertDimensionsMatch(input);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] *= input.data[i][j];
            }
        }
        return this;
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
        assertDimensionsMatch(input);
        Matrix result = new Matrix(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.data[i][j] = data[i][j] / input.data[i][j];
            }
        }

        return result;
    }

    /**
     * Matrix subtraction
     *
     * @param input
     * @return
     */
    public Matrix subtract(final Matrix input) {
        assertDimensionsMatch(input);

        Matrix result = new Matrix(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.data[i][j] = data[i][j] - input.data[i][j];
            }
        }

        return result;
    }

    public void applyOnEachElement(final MatrixElementFunction function) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                data[row][column] = function.transform(data[row][column], row, column);
            }
        }
    }

    public Vector getColumn(int column) {
        Vector result = new Vector(rows);
        for (int i = 0; i < rows; i++) {
            result.data[i] = data[i][column];
        }

        return result;
    }

    public double getRawCell(int row, int column) {
        return data[row][column];
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
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void assertDimensionsMatch(Matrix input) {
        if (!dimensionsMatch(input)) {
            throw new IllegalArgumentException("Dimensions do not match: expected rows " + rows + " found " + input.rows + ", expected columns " + columns + " found " + input.columns);
        }
    }

    public double set(int row, int column, double value) {
        double oldValue = this.data[row][column];
        this.data[row][column] = value;
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        return getRows() == matrix.getRows() &&
                getColumns() == matrix.getColumns() &&
                Arrays.equals(data, matrix.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getRows(), getColumns());
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
