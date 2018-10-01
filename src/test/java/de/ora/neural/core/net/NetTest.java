package de.ora.neural.core.net;

import de.ora.neural.core.activation.RectifiedLinearUnitsActivationFunction;
import de.ora.neural.core.activation.SigmoidActivationFunction;
import org.junit.Test;


public class NetTest {


    @Test
    public void propagate() {

        Net net = new Net()
                .addLayer(new Layer(2, 1, new SigmoidActivationFunction()));

        Vector result = net.propagate(new Vector(1, 0));

        net = new Net()
                .addLayer(new Layer(2, 10, new SigmoidActivationFunction()))
                .addLayer(new Layer(10, 5, new RectifiedLinearUnitsActivationFunction()));

        result = net.propagate(new Vector(1, 0));
    }
}
