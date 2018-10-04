package de.ora.neural.core.activation;

import de.ora.neural.core.net.Matrix;

public interface ActivationFunction {
    public double apply(double input);

    public Matrix apply(Matrix input);

    Matrix gradient(Matrix input);

    double derivate(double input);
}
