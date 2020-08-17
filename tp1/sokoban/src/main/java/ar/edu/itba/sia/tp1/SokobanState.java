package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.State;

import java.awt.Point;
import java.util.*;

public class SokobanState implements State, Cloneable {
    /**
     * State will be represented as a char matrix following
     * specifications here: [http://www.game-sokoban.com/index.php?mode=level_info&ulid=383&view=general]
     */

    public final static char EMPTY = 'e';
    public final static char BOX = 'b';
    public final static char WALL = 'w';



    /* static for one reference */
    public static char[][] map = null;// map with walls and goals that are static.
    public static char[][] areaMap = null;// map with areas defined that are static.
    public static  Set<Point> goals = null;

    private Point playerPosition;
    final private Set<Point> boxes;


    public SokobanState(Point playerPosition, Set<Point> boxes) {
        this.playerPosition = playerPosition;
        this.boxes = boxes;

    }

    public SokobanState( char[][] map, Point playerPosition, Set<Point> boxes, Set<Point> goals) {
        this.map = map;
        this.playerPosition = playerPosition;
        this.boxes = boxes;
        this.goals = goals;
    }

    /**
     * Returns Empty if configuration is not valid.
     * @param map
     * @return Optional of Sokoban State
     */
    public static Optional<SokobanState> initialState(char[][] map){

        Set<Point> goals = new HashSet<>();
        Set<Point> boxes=  new HashSet<>();
        Point playerPosition = null;
        SokobanState.map = map;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '@' || map[row][col] == '+') {
                    playerPosition = new Point(row, col);
                } else {
                    if (map[row][col] == '.' || map[row][col] == '*') {
                        goals.add(new Point(row, col));
                    }
                    if (map[row][col] == '$' || map[row][col] == '*') {
                        boxes.add(new Point(row, col));
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

        computeAreaMap();

        return Optional.of(new SokobanState(map, playerPosition, boxes, goals));
    }

    /**
     * calculate the static defined areas of the board
     * considering each area is connected by only one tunnel
     */
    private static void computeAreaMap() {
        // Java initializes char arrays in 0/
        areaMap = new char[areaMap.length][areaMap[0].length];
        Set<Integer> visitedAreas = new HashSet<>();
        char currentArea = 0;
        for (int row = 0; row < areaMap.length; row++) {
            for (int col = 0; col < areaMap[0].length; col++) {
                int areaId = visitedAreas.size();
                if (areaMap[row][col] != 'w' && !visitedAreas.contains(areaId)) {
                    visitArea(row, col, visitedAreas, areaId, false);
                }
            }
        }
    }

    private static boolean visitArea(int row, int col, Set<Integer> visitedAreas, int areaId, boolean tunnelVisited) {
        if (areaMap[row][col] == 'w') {
            return false;
        }
        if (row + 1 < areaMap.length && row - 1 >= 0
                && areaMap[row - 1][col] == 'w' && areaMap[row + 1][col] == 'w'
                && !tunnelVisited) {
            return true;
        }
        if (col + 1 < areaMap[0].length && col - 1 >= 0
                && areaMap[row][col - 1] == 'w' && areaMap[row][col + 1] == 'w'
                && !tunnelVisited) {
            return true;
        }

        areaMap[row][col] = (char)areaId;
        if (!visitedAreas.contains(areaId)) {
            visitedAreas.add(areaId);
        }
        int[][] dir ={{0,1},{-1,0},{0, -1},{0, 1}};
        for (int i = 0; i < 4; i++) {
            int targetRow = row + dir[i][0];
            int targetCol = col + dir[i][1];
            if (targetRow < areaMap.length && targetRow >= 0 &&
                    targetCol < areaMap[0].length && targetCol >= 0)  {
                tunnelVisited = visitArea(targetRow, targetCol, visitedAreas, areaId, tunnelVisited);
            }
        }
        return tunnelVisited;
    }

    public int getPlayerArea() {
        return areaMap[playerPosition.y][playerPosition.x];
    }

    public int getPositionArea(Point p) {
        return areaMap[p.y][p.x];
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {

        return 31 * getBoxes().hashCode() * getPlayerPosition().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SokobanState that = (SokobanState) o;
        return boxes.equals(that.getBoxes()) && playerPosition.equals(that.playerPosition);
    }


    @Override
    public String toString() {

        // TODO: rewrite
        StringBuilder s = new StringBuilder();
        s.append('[');
        s.append(playerPosition.x);
        s.append(',');
        s.append(playerPosition.y);
        s.append("]  -> ");
        boxes.stream().forEach( b-> {
            s.append("(");
            s.append(b.x);
            s.append(',');
            s.append(b.y);
            s.append(")");
            s.append('-');
        });

        return s.toString();
    }

    @Override
    public Object clone() {
        return new SokobanState(
                (Point) playerPosition.clone(),
                new HashSet<>(boxes)
        );
    }


    /**
     * Returns element in currrent state
     */
    public char getElement(Point a){

        if(map.length <= a.x
                || map[0].length <= a.y
                ||a.x <0 || a.y < 0){
            return WALL;
        }

        if(map[a.x][a.y] == '#'){
            // this is a wall
            return WALL;
        }
        if(boxes.contains(a)){
            return BOX;
        }

        return EMPTY;

    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public char[][] getMap() {
        return map;
    }

    public Set<Point> getBoxes() {
        return boxes;
    }

    public Set<Point> getGoals() {
        return goals;
    }

    public void setPlayerPosition(Point playerPosition) {
        this.playerPosition = playerPosition;
    }

}
