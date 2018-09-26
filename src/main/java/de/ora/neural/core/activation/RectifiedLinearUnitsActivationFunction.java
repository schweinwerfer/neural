package de.ora.neural.core.activation;

import de.ora.neural.core.net.Matrix;
import de.ora.neural.core.net.MatrixElementFunction;

public class RectifiedLinearUnitsActivationFunction implements ActivationFunction, MatrixElementFunction {
    @Override
    public double apply(double input) {
        return 0 > input ? 0 : input;
    }

    @Override
    public Matrix apply(Matrix input) {
        Matrix result = input.copy();
        result.applyOnEachElement(this);
        return result;
    }

    @Override
    public double transform(double input) {
        return apply(input);
    }
}
