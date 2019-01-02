package de.ora.neural.core.activation;

import de.ora.neural.core.net.Matrix;
import org.junit.Assert;
import org.junit.Test;


public class SigmoidActivationFunctionTest {

    @Test
    public void apply() {
        SigmoidActivationFunction function = new SigmoidActivationFunction();
        double result = function.apply(0.0);
        Assert.assertEquals(0.5, result, 0.001);
        result = function.apply(6.0);
        Assert.assertEquals(1.0, result, 0.01);
    }

    @Test
    public void applyMatrix() {
        SigmoidActivationFunction function = new SigmoidActivationFunction();
        Matrix matrix = new Matrix(3, 4).initWith(0.0);
        Matrix result = function.apply(matrix);
        for (double[] datum : result.data) {
            for (double v : datum) {
                Assert.assertEquals(0.5, v, 0.001);
            }
        }

        matrix = new Matrix(3, 4).initWith(6.0);
        result = function.apply(matrix);
        for (double[] datum : result.data) {
            for (double v : datum) {
                Assert.assertEquals(1.0, v, 0.01);
            }
        }
    }

    @Test
    public void gradient() {
    }
}
