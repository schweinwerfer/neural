package de.ora.tictactoe.genetic;

import de.ora.tictactoe.Board;
import de.ora.tictactoe.Coordinate;
import de.ora.tictactoe.GameResult;
import de.ora.tictactoe.Player;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayingAgent {
    private String name;
    private int generation;
    private Map<String, List<Coordinate>> moves = new HashMap<>();
    @JsonIgnore
    private Random rnd = new Random();
    private int winCnt = 0;
    private int loseCnt = 0;
    private int drawCnt = 0;
    @JsonIgnore
    private String lastMoveKey;
    @JsonIgnore
    private Set<String> winningMoveKeys = new HashSet<>();
    @JsonIgnore
    private Set<String> drawMoveKeys = new HashSet<>();

    protected PlayingAgent() {
    }

    public PlayingAgent(String name) {
        this.name = name;
        this.generation = 1;
    }

    public PlayingAgent(final PlayingAgent father, final PlayingAgent mother) {
        this.generation = ((father.generation + mother.generation) / 2) + 1;
        this.name = Math.abs((father.name + mother.name).hashCode()) + "-" + this.generation;

        int fatherMoveCnt = father.moves.size();
        int motherMoveCnt = mother.moves.size();

        father.addDominantMoves(this.moves);
        mother.addDominantMoves(this.moves);
        father.addDrawMoves(this.moves);
        mother.addDrawMoves(this.moves);

        if (fatherMoveCnt >= motherMoveCnt) {
            this.moves.putAll(pickRandomlyFrom(mother.moves, motherMoveCnt / 2));
//            this.moves.putAll(pickLinearFrom(mother.moves, motherMoveCnt / 2));
            fillWithMissingMoves(this.moves, father.moves);
        } else {
            this.moves.putAll(pickRandomlyFrom(father.moves, fatherMoveCnt / 2));
//            this.moves.putAll(pickLinearFrom(father.moves, fatherMoveCnt / 2));
            fillWithMissingMoves(this.moves, mother.moves);
        }

        if (rnd.nextInt(100) % 42 == 0) {
            Set<String> keysSet = this.moves.keySet();
            List<String> keys = new ArrayList<String>();
            keys.addAll(keysSet);
            this.moves.remove(keys.get(rnd.nextInt(keys.size())));
        }

    }

    private void addDrawMoves(final Map<String, List<Coordinate>> kidsMoves) {
        for (String key : drawMoveKeys) {
            if (kidsMoves.containsKey(key)) {
                continue;
            }
            kidsMoves.put(key, this.moves.get(key));
        }
    }

    private void addDominantMoves(final Map<String, List<Coordinate>> kidsMoves) {
        for (String key : winningMoveKeys) {
            kidsMoves.put(key, this.moves.get(key));
        }
    }

    public Coordinate play(final Player player, final Board board) {
        if (board.activePlayer() != player) {
            throw new IllegalArgumentException("Player " + player + " is not active");
        }

        final String key = board.asKey();
        List<Coordinate> possibleMoves = moves.get(key);
        if (possibleMoves == null) {
            possibleMoves = new ArrayList<>();
            moves.put(key, possibleMoves);
        }

        if (possibleMoves.isEmpty()) {
            List<Coordinate> freeCells = board.freeCells();
            if (freeCells.size() > 0) {
                // choose randomly one move
                possibleMoves.add(freeCells.get(rnd.nextInt(freeCells.size())));
            } else {
                throw new IllegalArgumentException("No free cell left on board for move!");
            }
        }

        Coordinate coordinate = possibleMoves.get(0);
        this.lastMoveKey = key;
        return coordinate;
    }

    public void feedback(final GameResult result) {
        switch (result) {
            case WON:
                this.winningMoveKeys.add(lastMoveKey);
                this.winCnt++;
                break;
            case LOST:
                this.loseCnt++;
                break;
            case DRAW:
                this.drawMoveKeys.add(lastMoveKey);
                this.drawCnt++;
                break;
        }
        this.lastMoveKey = null;
    }

    public String getName() {
        return name;
    }

    public double getFitness() {
        double successCnt = winCnt + (double) drawCnt;
        double playedGamesCnt = successCnt + loseCnt;

        return successCnt / playedGamesCnt;
    }

    public String getDetailedScore() {
        return String.valueOf(getFitness()) + " (w: " + winCnt + ", l: " + loseCnt + ", d: " + drawCnt + ")";
    }

    public void resetScore() {
        this.lastMoveKey = null;
        this.winningMoveKeys.clear();
        this.drawMoveKeys.clear();
        this.winCnt = 0;
        this.loseCnt = 0;
        this.drawCnt = 0;
    }

    private void fillWithMissingMoves(Map<String, List<Coordinate>> kidsMoves, Map<String, List<Coordinate>> parentMoves) {
        int parentMovesSize = parentMoves.size();
        int kidsMovesSize = kidsMoves.size();

        if (parentMovesSize <= kidsMovesSize) {
            return;
        }

        while (kidsMoves.size() < parentMovesSize) {
            for (Map.Entry<String, List<Coordinate>> entry : parentMoves.entrySet()) {
                if (!kidsMoves.containsKey(entry.getKey())) {
                    kidsMoves.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private Map<String, List<Coordinate>> pickRandomlyFrom(Map<String, List<Coordinate>> map, int count) {
        Map<String, List<Coordinate>> result = new HashMap<>();

        List<Map.Entry<String, List<Coordinate>>> entries = new ArrayList<>(map.entrySet());

        while (result.size() < count) {
            Map.Entry<String, List<Coordinate>> picked = entries.get(rnd.nextInt(entries.size()));
            if (!result.containsKey(picked.getKey())) {
                result.put(picked.getKey(), picked.getValue());
            }
        }

        return result;
    }

    private Map<String, List<Coordinate>> pickLinearFrom(Map<String, List<Coordinate>> map, int count) {
        Map<String, List<Coordinate>> result = new HashMap<>();

        List<Map.Entry<String, List<Coordinate>>> entries = new ArrayList<>(map.entrySet());

        for (int i = 0; i < count; i++) {
            Map.Entry<String, List<Coordinate>> picked = entries.get(i);
            if (!result.containsKey(picked.getKey())) {
                result.put(picked.getKey(), picked.getValue());
            }
        }

        return result;
    }

    public int getGeneration() {
        return generation;
    }

    public File store(final File dir, final String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File resultFile = new File(dir, filename);
        mapper.writeValue(resultFile, this);

        return resultFile;
    }

    public File store(final File dir) throws IOException {
        return store(dir, "agent_" + winCnt + "-" + drawCnt + "-" + loseCnt + "_" + System.currentTimeMillis() + ".json");
    }

    public File store(final String filename) throws IOException {
        File file = new File(filename + ".json");
        return store(file);
    }

    public static PlayingAgent load(final File file) throws IOException {
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, PlayingAgent.class);
    }

    public static PlayingAgent load(final String filename) throws IOException {
        File file = new File(filename + ".json");
        return load(file);
    }

    public Map<String, List<Coordinate>> getMoves() {
        return moves;
    }

    public int getWinCnt() {
        return winCnt;
    }

    public int getLoseCnt() {
        return loseCnt;
    }

    public int getDrawCnt() {
        return drawCnt;
    }
}
