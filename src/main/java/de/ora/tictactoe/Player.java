package de.ora.tictactoe;

import java.util.HashMap;
import java.util.Map;

public enum Player {
    NONE(0),
    PLAYER1(1),
    PLAYER2(2);
    private int code;
    private static final Map<Integer, Player> lookup;


    static {
        lookup = new HashMap<>();

        for (Player player : Player.values()) {
            lookup.put(player.getCode(), player);
        }
    }


    Player(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Player from(int code) {
        return lookup.get(code);
    }
}
