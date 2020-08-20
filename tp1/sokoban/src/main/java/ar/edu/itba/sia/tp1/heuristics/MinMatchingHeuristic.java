package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanProblem;
import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;
import ar.edu.itba.sia.tp1.utils.MapAnalyzer;

import java.awt.Point;
import java.util.*;
/**
 * MinMatching algorithm implemented using KM algorithm
 */
public class MinMatchingHeuristic implements Heuristic {

    Map<Point,int[][]> goalsDistSegments;

    // Le falta los 2 ulitmos pasos el algoritmo KM
    @Override
    public double getValue(State s) {

        SokobanState ss = (SokobanState) s;

        if(goalsDistSegments == null){
            goalsDistSegments = MapAnalyzer.calculateDistances(ss.getMap(), ss.getGoals());
        }

        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }
        ss = (SokobanState) ss.clone();
        int n = ss.getGoals().size();

        List<Point> goalsList = new ArrayList<Point>();
        goalsList.addAll(ss.getGoals());

        List<Point> boxList = new ArrayList<Point>();
        boxList.addAll(ss.getBoxes());

        // n + 1 length for storing marks
        int[][][] adjMatrix = new int[n + 1][n + 1][2];
        // n + 1 length for storing marks
        int[][] originalAdjMatrix = new int[n][n];
        // Step 1
        for (int i = 0; i < n; i++) {
            int minRow = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j][0] = getMinimumPush(boxList.get(i), goalsList.get(j), ss);
                originalAdjMatrix[i][j] = adjMatrix[i][j][0];
                if (adjMatrix[i][j][0] <= minRow) {
                    minRow = adjMatrix[i][j][0];
                }
            }
            // Subtract minimum row value to each value in the row
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j][0] -= minRow;
            }
        }
        // Step 2
        for (int j = 0; j < n; j++) {
            int minCol = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (adjMatrix[i][j][0] <= minCol) {
                    minCol = adjMatrix[i][j][0];
                }
            }
            // Subtract minimum column value to each value in the column
            for (int i = 0; i < n; i++) {
                adjMatrix[i][j][0] -= minCol;
            }
        }

        int lineCount = 0;
        boolean[] coveredRows = new boolean[n];
        boolean[] coveredCols = new boolean[n];
        boolean[] assignedRows = new boolean[n];
        boolean[] assignedCols = new boolean[n];
        while (lineCount < n) {
            lineCount = 0;
            Arrays.fill(coveredCols, false);
            Arrays.fill(coveredRows, false);
            Arrays.fill(assignedCols, false);
            Arrays.fill(assignedRows, false);
            // Step 3
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (adjMatrix[i][j][0] == 0 && !assignedCols[j] && !assignedRows[i]) {
                        // Assign 0
                        adjMatrix[i][j][1] = 'a';
                        assignedCols[j] = true;
                        assignedRows[i] = true;
                    }
                    else if (adjMatrix[i][j][0] == 0 && (assignedCols[j] || assignedRows[i])) {
                        // Star 0
                        adjMatrix[i][j][1] = 'x';
                    }
                }
            }

            Set<Integer> newlyMarkedRows = new HashSet<>();
            Set<Integer> newlyMarkedColumns = new HashSet<>();
            // Mark all rows having no assignments
            for (int i = 0; i < n; i++) {
                boolean assigned = false;
                for (int j = 0; j < n; j++) {
                    if (adjMatrix[i][j][1] == 'a') {
                        assigned = true;
                        break;
                    }
                }
                if (!assigned) {
                    adjMatrix[i][n][0] = 'm';
                    newlyMarkedRows.add(i);
                }
            }

            // Until there are no new rows or columns being marked
            while (!newlyMarkedColumns.isEmpty() || !newlyMarkedRows.isEmpty()) {
                // Mark all columns having stars in newly marked row(s)
                for (int j = 0; j < n; j++) {
                    for (int i = 0; i < n; i++) {
                        if (adjMatrix[i][j][1] == 'x' && newlyMarkedRows.contains(i)) {
                            adjMatrix[n][j][0] = 'm';
                            newlyMarkedColumns.add(j);
                            coveredCols[j] = true;
                            break;
                        }
                    }
                }
                newlyMarkedRows.clear();
                // Mark all rows having assignments in newly marked columns
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (adjMatrix[i][j][1] == 'a' && newlyMarkedColumns.contains(j)) {
                            adjMatrix[i][n][0] = 'm';
                            newlyMarkedRows.add(i);
                            break;
                        }
                    }
                }
                newlyMarkedColumns.clear();
            }
            // draw lines through all marked columns and unmarked rows
            for (int i = 0; i < n; i++) {
                if (adjMatrix[i][n][0] != 'm') {
                    coveredRows[i] = true;
                    lineCount++;
                }
            }
            for (int j = 0; j < n; j++) {
                if (adjMatrix[n][j][0] == 'm') {
                    coveredCols[j] = true;
                    lineCount++;
                }
            }
            // Step 5
            if (lineCount < n) {
                // Find the smallest entry not covered by any line
                int minValue = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (!coveredRows[i] && !coveredCols[j] && adjMatrix[i][j][0] <= minValue) {
                            minValue = adjMatrix[i][j][0];

                        }
                    }
                }
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        // Subtract this entry from each row that isn’t crossed out (or each row that has been marked)
                        if (adjMatrix[i][n][0] == 'm') {
                            adjMatrix[i][j][0] -= minValue;
                        }
                        // Then add it to each column that is crossed out (or each column that has been marked)
                        if (adjMatrix[n][j][0] == 'm') {
                            adjMatrix[i][j][0] += minValue;
                        }
                    }
                }
            }
        }

        int minimumCost = 0;
        // The minimum cost is the sum of all the cost where are 0 that each row an column ont intersect
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ( !(adjMatrix[n][j][0] == 'm' && adjMatrix[i][n][0] != 'm')
                        && adjMatrix[n][j][0] == 'm' && adjMatrix[i][n][0] == 'm' && adjMatrix[i][j][1] == 'x'
                        || adjMatrix[n][j][0] != 'm' && adjMatrix[i][n][0] != 'm' && adjMatrix[i][j][1] == 'a') {
                    minimumCost += originalAdjMatrix[i][j];
                }
            }
        }
        return minimumCost * SokobanProblem.COST;
    }

    @Override
    public Class getModifierRulesClass() {
        return Push.class;
    }

    private int getMinimumPush(Point box, Point goal, SokobanState ss) {
        int pushes = 0;

        pushes = goalsDistSegments.get(goal)[box.x][box.y];

        return pushes;
    }
     
}
