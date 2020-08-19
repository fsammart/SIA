package ar.edu.itba.sia.tp1.utils;

import ar.edu.itba.sia.tp1.SokobanState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class LockAnalyzer {
    private static Set<Point> lockPositions;

    private static Map<Set<Point>, Integer> lockSpace;

    public static final void lockInitialize(SokobanState s){
        lockPositions = new HashSet<>();
        lockSpace = new HashMap<>();

        searchLockPositions(s);
    }

    public static final boolean isLock(SokobanState s){

        Set<Point> boxes = s.getBoxes();

        boolean r =boxes.stream()
                .filter(p -> !SokobanState.goals.contains(p))
                .anyMatch(lockPositions::contains) ;
        if(r) {return true;}

        boolean temporalBlock =boxes.stream()
                .filter(p -> !SokobanState.goals.contains(p))
                .allMatch(s::isTemporallyBlocked);
        if(temporalBlock && !SokobanState.goals.containsAll(boxes)) {
            return true;
        }

        for(Set<Point> l : lockSpace.keySet()){
            if(boxes.stream()
                    .filter(l::contains)
                    .count() > lockSpace.get(l)){
                return true;
            }
        }

        return false;
    }

    /**
     * Analyze map to check for blocking positions
     * Corners and walls.
     */
    private static final void searchLockPositions(SokobanState s) {
        char[][] map = s.getMap();

        for(int row = 1; row < map.length - 1; row++) {
            for(int col = 1; col < map[0].length - 1; col++) {
                if(map[row][col] != '#') {
                    analyzeStaticDeadlocks(map, row, col);
                }
            }
        }

    }

    //Analyze four possible deadlocks created by corners: ##  ##  #    # (if there is no goal)
    //					  								   #  #   ##  ##
    private static void analyzeStaticDeadlocks(char[][] map, int row, int col){
        boolean lock = false;
        if(map[row][col] == ' ' || map[row][col] == '$' || map[row][col] == '@') {
            lock = true;
        }
        boolean up = map[row-1][col] == '#';
        boolean down = map[row+1][col] == '#';
        boolean right = map[row][col+1] == '#';
        boolean left = map[row][col-1] == '#';

        if(up && right){
            if(lock) lockPositions.add(new Point(row, col));
            analyzeStaticColumnDeadlocks(map,col,1,row);
        }
        if(left && up) {
            if(lock) lockPositions.add(new Point(row, col));
            analyzeStaticColumnDeadlocks(map,col,-1,row);
            analyzeStaticRowDeadlocks(map,row,-1,col);
        }
        if(right && down){
            if(lock) lockPositions.add(new Point(row, col));
        }

        if(down && left){
            if(lock) lockPositions.add(new Point(row, col));
            analyzeStaticRowDeadlocks(map,row,1,col);

        }



    }

    /**
     *
     * @param map
     * @param col
     * @param direction : indicates dy (col right or left) where wall should be found
     */
    private static void analyzeStaticColumnDeadlocks(char[][] map, int col, int direction, int start) {
        Set<Point> points = new HashSet<>();
        int countGoals = 0;
        for (int i = start; i < map.length; i++) {
            if(map[i][col] == '#') {
                // found other corner
                lockSpace.put(points,countGoals);
                return;
            }

            if (map[i][col + direction] == '#') {
                if(map[i][col] == '.' || map[i][col] == '*'){
                    countGoals++;
                }
                points.add(new Point(i,col));

            } else {
                return;
            }



        }
    }

    /**
     *
     * @param map
     * @param row
     * @param direction: indicates dx (row up or down) where wall should be found
     */
    private static void analyzeStaticRowDeadlocks(char[][] map, int row, int direction, int start){
        Set<Point> points = new HashSet<>();
        int countGoals = 0;
        for(int i = start; i < map[0].length; i++) {

            if(map[row][i] == '#') {
                // found other corner
                lockSpace.put(points,countGoals);
                return;
            }

            if (map[row+direction][i] == '#') {
                if(map[row][i] == '.' || map[row][i] == '*'){
                    countGoals++;
                }
                points.add(new Point(row,i));

            } else {
                return;
            }



        }
    }



}
