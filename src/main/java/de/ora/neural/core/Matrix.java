package de.ora.neural.core;

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

    public void initZeros() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                data[i][j] = 0;
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

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
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
