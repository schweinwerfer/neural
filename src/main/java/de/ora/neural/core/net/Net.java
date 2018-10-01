package de.ora.neural.core.net;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class Net {
    Deque<Layer> layers = new LinkedList<>();

    public Net() {
    }

    public Net addLayer(final Layer layer) {
        if (!layers.isEmpty() && !layer.isCompatibleTo(layers.peek())) {
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
}
