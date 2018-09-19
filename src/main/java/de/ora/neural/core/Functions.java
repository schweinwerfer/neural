package de.ora.neural.core;

public class Functions {

    /**
     * Rectified linear unit
     *
     * @param input
     * @return max(0, input)
     */
    public static double rectifiedLU(double input) {
        return 0 > input ? 0 : input;
    }
}
