package de.ora.neural.core.net;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class Net {
    Deque<Layer> layers = new LinkedList<>();
    private double currentCost;

    public Net() {
    }

    public Net addLayer(final Layer layer) {
        if (!layers.isEmpty() && !layer.isCompatibleTo(layers.getLast())) {
            throw new IllegalArgumentException("Provided layer is not compatible to previous layer!");
        }

        layers.add(layer);

        return this;
    }

    public Vector propagate(final Vector input) {
        final Iterator<Layer> iterator = layers.iterator();

        Vector nextInput = input;

        Layer next;
        while (iterator.hasNext()) {
            next = iterator.next();
            nextInput = next.propagate(nextInput);
        }

        return nextInput;
    }

    public double train(final Vector input, final Vector expectedOutput) {
        final Vector output = propagate(input);
        Vector costVector = output.subtract(expectedOutput).pow(2);
        currentCost = costVector.sumElements() * 0.5;


        return currentCost;
    }

    public double currentCost() {
        return currentCost;
    }
}
