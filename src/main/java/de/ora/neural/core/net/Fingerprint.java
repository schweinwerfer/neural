package de.ora.neural.core.net;

public class Fingerprint implements Comparable<Fingerprint> {
    private final int rows;
    private final int columns;
    private Integer value;
    private FPOps[] ops;

    public Fingerprint(int rows, int columns, Integer value, FPOps... ops) {
        this.value = value;
        this.ops = ops;
        this.rows = rows;
        this.columns = columns;
    }

    public Integer getValue() {
        return value;
    }

    public FPOps[] getOps() {
        return ops;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public int compareTo(Fingerprint o) {
        return this.value.compareTo(o.value);
    }
}
