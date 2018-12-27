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
    public Matrix apply(final Matrix input) {
        Matrix result = input.copy();
        Matrix ones = new Matrix(result.getRows(), result.getColumns()).initOnes();
        result.multiply(-1); // -x
        result.exp(); // e^(-x)
        result.add(1);// 1 + e^(-x)
        return ones.divide(result); // 1 / (1 + e^(-x))
    }

    @Override
    public Matrix gradient(final Matrix input) {
        Matrix result = input.copy();
        Matrix ones = new Matrix(result.getRows(), result.getColumns()).initOnes();
        return result.elemMultiply(ones.subtract(result));
    }

    @Override
    public double derivate(double input) {
        double sigma = apply(input);
        return sigma * (1 - sigma);
    }
}
