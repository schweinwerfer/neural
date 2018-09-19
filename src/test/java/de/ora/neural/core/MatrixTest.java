package de.ora.neural.core;

public class MatrixTest {


    @org.junit.Test
    public void multiply() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        Vector vector = new Vector(new double[]{1, 2, 3});
        Matrix m = new Matrix(matrix);
        System.out.print(m);
        Vector result = m.multiply(vector);
        System.out.print(result);
        Vector bias = new Vector(new double[]{0.5, 0.75});
        result = result.add(bias);
        System.out.print(result);
    }
}
