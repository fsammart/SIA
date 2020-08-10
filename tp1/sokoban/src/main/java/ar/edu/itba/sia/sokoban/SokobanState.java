package ar.edu.itba.sia.sokoban;

import ar.edu.itba.sia.solver.api.State;

import java.awt.Point;
import java.util.*;

public class SokobanState implements State, Cloneable {
    /**
     * State will be represented as a char matrix following
     * specifications here: [http://www.game-sokoban.com/index.php?mode=level_info&ulid=383&view=general]
     */

    /**
     * goals do not change throughout the game.
     */
    private static List<Point> goals;

    final private char[][] map;

    final private List<Point> boxes;

    final private Point playerPosition;


    public SokobanState( char[][] map, List<Point> boxes, Point playerPosition) {
        this.boxes = boxes;
        this.map = map;
        this.playerPosition = playerPosition;
    }

    /**
     * Returns Empty if configuration is not valid.
     * @param map
     * @return Optional of Sokoban State
     */
    public static Optional<SokobanState> initialState(char[][] map){

        List<Point> goals = new ArrayList<>();
        List<Point> boxes=  new ArrayList<>();
        Point playerPosition = null;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '@' || map[row][col] == '+') {
                    playerPosition = new Point(col, row);
                } else {
                    if (map[row][col] == '.' || map[row][col] == '*') {
                        goals.add(new Point(col, row));
                    }
                    if (map[row][col] == '$' || map[row][col] == '*') {
                        boxes.add(new Point(col, row));
                    }
                }
            }
        }

        /**
         * check for Invalid configurations
         */
        if(playerPosition == null || goals.isEmpty() ||
                boxes.contains(playerPosition)) {
           return Optional.empty();

        }

        SokobanState.goals = goals;

        return Optional.of(new SokobanState(map, boxes, playerPosition));
    }


    /**
     *
     * @return
     */
    @Override
    public int hashcode() {
        return Arrays.deepHashCode(map);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SokobanState that = (SokobanState) o;
        return Arrays.deepEquals(map, that.map);
    }


    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        Arrays.stream(map).forEach( row -> {
            s.append(row);
            s.append('\n');
        });

        return s.toString();
    }

    @Override
    public Object clone() {

        int length = map.length;
        char[][] target = new char[length][map[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(map[i], 0, target[i], 0, map[i].length);
        }

        return new SokobanState(
                target,
                new ArrayList<>(boxes),
                (Point) playerPosition.clone()
        );
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public char[][] getMap() {
        return map;
    }
}
