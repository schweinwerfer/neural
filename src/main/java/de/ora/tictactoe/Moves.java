package de.ora.tictactoe;

import de.ora.neural.core.net.FPOps;
import de.ora.neural.core.net.Fingerprint;

import java.util.LinkedList;
import java.util.List;

public class Moves {
    private List<Fingerprint> fingerprints;
    private List<Coordinate> moves = new LinkedList<>();

    public Moves(List<Fingerprint> fingerprints) {
        this.fingerprints = fingerprints;
    }

    public Fingerprint getRepresentant() {
        return fingerprints.get(0);
    }

    public Coordinate getMoveFor(Fingerprint fingerprint) {
        final Coordinate coordinate = moves.get(0);
        Coordinate result = coordinate;
        for (FPOps ops : fingerprint.getOps()) {
            switch (ops) {
                case ROT:
                    result = result.rotate(fingerprint.getRows());
                    break;
                case MX:
                    result = result.mirrorX(fingerprint.getColumns());
                    break;

                case NOP:
                    break;
            }
        }

        return result;
    }
}
