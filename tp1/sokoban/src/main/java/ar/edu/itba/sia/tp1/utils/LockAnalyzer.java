package ar.edu.itba.sia.tp1.utils;

import ar.edu.itba.sia.tp1.SokobanState;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class LockAnalyzer {
    private static Set<Point> lockPositions;
    private static List<Point> lockRowSpace ;
    private static List<Point> lockColumnSpace;

    public static final void lockInitialize(SokobanState s){
        lockPositions = new HashSet<>();
        lockRowSpace = new ArrayList<>(s.getMap().length);
        lockColumnSpace = new ArrayList<>(s.getMap()[0].length);
        searchLockPositions(s);
    }

    public static final boolean isLock(SokobanState s){

        Set<Point> boxes = s.getBoxes();

        boolean r =boxes.stream()
                .filter(p -> !SokobanState.goals.contains(p))
                .anyMatch(lockPositions::contains) ;
        if(r) {return true;}
        r = lockRowSpace.stream()
                .anyMatch(a ->boxes.stream()
                        .filter(p ->  p.x == a.x)
                        .count() > a.y);
        if(r) {return true;}
        r = lockColumnSpace.stream()
                .anyMatch(a -> boxes.stream()
                        .filter(p ->  p.y == a.x)
                        .count() > a.y);

        return r;
    }

    /**
     * Analyze map to check for blocking positions
     * Corners and walls.
     */
    private static final void searchLockPositions(SokobanState s) {
        char[][] map = s.getMap();

        for(int row = 1; row < map.length - 1; row++) {
            analyzeStaticRightRowDeadlocks(map, row,  -1);
            analyzeStaticRightRowDeadlocks(map, row,  1);
            for(int col = 1; col < map[0].length - 1; col++) {
                analyzeStaticDeadlocks(map, row, col);
            }
        }
        for(int col = 1; col < map[0].length - 1; col++) {
            analyzeStaticDownRowDeadlocks(map,  col, -1);
            analyzeStaticDownRowDeadlocks(map,  col, 1);
        }

    }
    //Analyze four possible deadlocks created by corners: ##  ##  #    # (if there is no goal)
    //					  								   #  #   ##  ##
    private static void analyzeStaticDeadlocks(char[][] map, int row, int col){
        if(map[row][col] == ' ' || map[row][col] == '$' || map[row][col] == '@') {
            boolean up = map[row-1][col] == '#';
            boolean down = map[row+1][col] == '#';
            boolean right = map[row][col+1] == '#';
            boolean left = map[row][col-1] == '#';

            if((up && right) || (left && up)){
                lockPositions.add(new Point(row, col));
            }

            if((right && down) || (down && left)){
                lockPositions.add(new Point(row, col));

            }

        }

    }

    //Analyze the deadlocks created by a row of walls (if there is no goal)
	/*		Deadlock
			With direction +1
			##########
					D#
					D#
					##

			With direction -1
			##########
			#D
			#D
			#D
			##
	 */
    private static void analyzeStaticDownRowDeadlocks(char[][] map, int col, int wallDir) {
        int countGoals = 0;
        for (int i = 1; i < map.length; i++) {

            if (map[i][col + wallDir] == '#') {
                if(map[i][col] == '.' || map[i][col] == '*'){
                    countGoals++;
                }

            } else {
                return;
            }

        }
        lockColumnSpace.add(new Point(col,countGoals));
    }

    //Analyze the deadlocks created by a row of walls (if there is no goal)
	/*		Deadlock
			With direction +1
			#DDDDDDDD#
			##########

			With direction -1
			##########
			#DDDDDDDD#

	 */
    private static void analyzeStaticRightRowDeadlocks(char[][] map, int row, int wallDir){
        int countGoals = 0;
        for(int i = 1; i < map[0].length; i++) {

            if (map[row+wallDir][i] == '#') {
                if(map[row][i] == '.' || map[row][i] == '*'){
                    countGoals++;
                }

            } else {
                return;
            }

        }
        lockRowSpace.add(new Point(row,countGoals));
    }



}
