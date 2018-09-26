package de.ora.neural.core.activation;

import de.ora.neural.core.net.Matrix;

/**
 * <pre>
 *  S(t) =       1
 *             ------------
 *             1 + e^(-t)
 * </pre>
 */
public class SigmoidActivationFunction implements ActivationFunction {
    @Override
    public double apply(double input) {
        double y;
        if (input < -10)
            y = 0;
        else if (input > 10)
            y = 1;
        else
            y = 1.0 / (1.0 + Math.exp(-input));
        return y;
    }

    @Override
    public Matrix apply(Matrix input) {
        Matrix result = input.copy();
        Matrix ones = new Matrix(result.getRows(), result.getColumns()).initOnes();
        result.multiply(-1);
        result.exp();
        result.add(1);
        return ones.divide(result);
    }
}
