package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.heuristics.ManhattanDistanceObstacles;
import ar.edu.itba.sia.tp1.utils.MapAnalyzer;

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
    //public static char[][] areaMap = null;// map with areas defined that are static.
    public static  Set<Point> goals = null;

    private Point playerPosition;
    final private Set<Point> boxes;

    static Set<Point> unReachable = new HashSet<>();


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

        int [][] distMap = MapAnalyzer.calculateReachable(map,goals);

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if(distMap[row][col] == -1){
                    if (map[row][col] != ' ') {
                        throw new IllegalArgumentException("Wrong Map configuration");
                    }

                    map[row][col] = '#';
                    unReachable.add(new Point(row,col));
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

        //computeAreaMap();

        return Optional.of(new SokobanState(map, playerPosition, boxes, goals));
    }

    public boolean isTemporallyBlocked(Point box){
        Point up = new Point(box.x - 1,box.y);
        Point right = new Point(box.x ,box.y + 1);
        Point down = new Point(box.x + 1,box.y);
        Point left = new Point(box.x ,box.y -1);

        boolean upBlock = this.getElement(up) != EMPTY;
        boolean downBlock = this.getElement(down) != EMPTY;
        boolean leftBlock = this.getElement(left) != EMPTY;
        boolean rightBlock = this.getElement(right) != EMPTY;

        // up and right
        if(upBlock && rightBlock) return true;
        //up and left
        if(upBlock && leftBlock) return true;
        //left and down
        if(downBlock && leftBlock) return true;
        //down and right
        if(downBlock && rightBlock) return true;

        return false;
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

        Point player = playerPosition;
        for(int i= 0; i < map.length; i++){
            for(int j =0; j < map[0].length; j++){
                Point p = new Point(i,j);
                boolean isPlayer = p.equals(player);
                boolean isBox = boxes.contains(p);
                boolean isGoal = goals.contains(p);
                boolean isUnreachable = unReachable.contains(p);
                boolean isWall = (map[i][j] == '#');
                if(isWall){
                    if(isUnreachable){
                        s.append(' ');
                    }else{
                        s.append('#');
                    }
                } else if(isBox){
                    if(isGoal){
                        s.append('*');
                    }else{
                        s.append('$');
                    }
                } else if(isGoal){
                    if(isPlayer){
                        s.append('+');
                    }else{
                        s.append('.');
                    }
                } else if(isPlayer){
                    s.append('@');
                }else{
                    s.append(' ');
                }
            }
            s.append('\n');
        }

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
