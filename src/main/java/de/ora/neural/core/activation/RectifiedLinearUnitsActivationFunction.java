package de.ora.neural.core.activation;

import de.ora.neural.core.net.Matrix;
import de.ora.neural.core.net.MatrixElementFunction;

public class RectifiedLinearUnitsActivationFunction implements ActivationFunction {
    @Override
    public double apply(double input) {
        return 0 > input ? 0 : input;
    }

    @Override
    public Matrix apply(Matrix input) {
        Matrix result = input.copy();
        result.applyOnEachElement(new MatrixElementFunction() {
            @Override
            public double transform(double input, int row, int column) {
                return 0 > input ? 0 : input;
            }
        });
        return result;
    }


    @Override
    public Matrix gradient(final Matrix input) {
        Matrix result = input.copy();
        result.applyOnEachElement(new MatrixElementFunction() {
            @Override
            public double transform(double input, int row, int column) {
                return input >= 0 ? 1 : 0;
            }
        });
        return null;
    }

    @Override
    public double derivate(double input) {
        return input >= 0 ? 1 : 0;
    }
}
