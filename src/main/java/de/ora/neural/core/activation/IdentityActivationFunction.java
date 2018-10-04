package de.ora.neural.core.activation;

import de.ora.neural.core.net.Matrix;

/**
 * <pre>
 *  S(t) =  t
 * </pre>
 */
public class IdentityActivationFunction implements ActivationFunction {
    @Override
    public double apply(double input) {
        return input;
    }

    @Override
    public Matrix apply(final Matrix input) {
        return input;
    }

    @Override
    public Matrix gradient(Matrix input) {
        return new Matrix(input.getRows(), input.getColumns()).initWith(1);
    }

    @Override
    public double derivate(double input) {
        return 1;
    }


}
