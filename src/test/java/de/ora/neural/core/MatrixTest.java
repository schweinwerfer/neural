package de.ora.neural.core;

import de.ora.neural.core.net.GenericMatrix;
import de.ora.neural.core.net.Matrix;
import de.ora.neural.core.net.Vector;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void testMatrix() {
        GenericMatrix<Integer> matrix = new GenericMatrix<>(3, 3);
        matrix.set(0, 0, 1);
        matrix.set(1, 1, 1);
        matrix.set(1, 2, 2);

        final int hashValue = GenericMatrix.hashValue(matrix);

        final GenericMatrix<Integer> rotate = matrix.rotate();
        final GenericMatrix<Integer> rotate1 = rotate.rotate();
        final GenericMatrix<Integer> rotate2 = rotate1.rotate();

        final List<Integer> fingerprints = matrix.fingerprints();

    }
}
