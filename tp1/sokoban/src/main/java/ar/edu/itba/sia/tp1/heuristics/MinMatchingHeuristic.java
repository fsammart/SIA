package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class MinMatchingHeuristic implements Heuristic {

    // Le falta los 2 ulitmos pasos el algoritmo KM
    @Override
    public double getValue(State s) {
        SokobanState ss = (SokobanState) s;
        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }

        int n = ss.getGoals().size();

        List<Point> goalsList = new ArrayList<Point>();
        goalsList.addAll(ss.getGoals());

        List<Point> boxList = new ArrayList<Point>();
        boxList.addAll(ss.getBoxes());

        // Minimum columns an rows values are stored in extra row/column
        int[][] adjMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            int minRow = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j] = getMinimumPush(boxList.get(i), goalsList.get(j), ss);
                if (adjMatrix[i][j] <= minRow) {
                    minRow = adjMatrix[i][j];
                }
            }
            // Subtract minimum row value to each value in the row
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j] -= minRow;
            }
        }

        for (int j = 0; j < n; j++) {
            int minCol = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (adjMatrix[i][j] <= minCol) {
                    minCol = adjMatrix[i][j];
                }
            }
            // Subtract minimum column value to each value in the column
            for (int i = 0; i < n; i++) {
                adjMatrix[i][j] -= minCol;
            }
        }

        return 0;
    }

    private int getMinimumPush(Point box, Point goal, SokobanState ss) {
        int pushes = 0;
        // Run manhattan path
        int dx = goal.x - box.x;
        int colStep = Integer.signum(dx);
        int boxCol = box.x;
        int boxRow = box.y;
        int dy = goal.y - box.y;
        int rowStep = Integer.signum(dy);

        pushes = Math.abs(dx) + Math.abs(dy);

        /*

        int aux = boxCol;
        //Check for backouts
        while (!playerCanPushBox(boxCol, boxRow, colStep, 0, ss)) {
            if (playerCanPushBox(boxCol, boxRow, - colStep, 0, ss)) {
                ss.setPlayerPosition(new Point(boxCol, boxRow));
                boxCol -= colStep;
            } else {
                boxCol = aux;
                break;
            }
        }
        aux = boxRow;
        while (!playerCanPushBox(boxCol, boxRow, 0, rowStep, ss)) {
            if (playerCanPushBox(boxCol, boxRow, 0, -rowStep, ss)) {
                ss.setPlayerPosition(new Point(boxCol, boxRow));
                boxRow -= rowStep;
            } else {
                boxRow = aux;
                break;
            }
        }

        // Check for linear conflicts
        while (boxRow == goal.y && boxCol != goal.x) {
            if (ss.getBoxes().contains(new Point(boxCol, boxRow))) {
                pushes += 2;
                break;
            }
            boxCol += colStep;
        }
        while (boxCol == goal.x && boxRow != goal.y) {
            if (ss.getBoxes().contains(new Point(boxCol, boxRow))) {
                pushes += 2;
                break;
            }
            boxRow += rowStep;
        }
        */

        return pushes;


    }
    /*
    private boolean playerCanPushBox(int col, int row, int colStep, int rowStep, SokobanState ss) {
        if (ss.getMap()[col + colStep][row + rowStep] == 'w'
                || ss.getMap()[col - colStep][row - rowStep] == 'w') {
            return false;
        }
        if (ss.getPlayerArea() != ss.getPositionArea(new Point(col - colStep, row - rowStep))) {
            return false;
        }
        return true;
    }
    */

    @Override
    public Class getModifierRulesClass() {
        return Push.class;
    }
}
