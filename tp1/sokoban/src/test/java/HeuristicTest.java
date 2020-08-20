import ar.edu.itba.sia.tp1.SokobanProblem;
import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.heuristics.*;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HeuristicTest {

    char [][] map1 = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '*', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };
    char [][] map2 = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', ' ', '$', ' ', ' ', ' ', ' ', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '.', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] lockState = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', '$', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '.', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    private SokobanState emptyGoals1;
    private SokobanState emptyGoals2;
    private SokobanState lock;

    private int COST = SokobanProblem.COST;

    @org.junit.Before
    public void setUp() throws Exception {
        emptyGoals1 = SokobanState.initialState(map1).get();
        emptyGoals2 = SokobanState.initialState(map2).get();
        lock = SokobanState.initialState(lockState).get();

    }

    @org.junit.Test
    public void emptyGoals1() throws Exception {
        SokobanState.map = map1;
        LockAnalyzer.lockInitialize(emptyGoals1);
        EmptyGoalsHeuristic e = new EmptyGoalsHeuristic();
        assertEquals(1 * COST,e.getValue(emptyGoals1),0.1);
    }

    @org.junit.Test
    public void emptyGoals2() throws Exception {
        SokobanState.map = map2;
        LockAnalyzer.lockInitialize(emptyGoals2);
        EmptyGoalsHeuristic e = new EmptyGoalsHeuristic();
        assertEquals(2 * COST, e.getValue(emptyGoals2), 0.1);
    }

    @org.junit.Test
    public void returnsMaxValueLocked() throws Exception {
        SokobanState.map = lockState;
        LockAnalyzer.lockInitialize(lock);
        EmptyGoalsHeuristic e = new EmptyGoalsHeuristic();
        assertEquals(Double.MAX_VALUE, e.getValue(lock), 0.1);
    }

    @org.junit.Test
    public void minimumDistance() throws Exception {
        SokobanState.map = map2;
        LockAnalyzer.lockInitialize(emptyGoals2);
        MinimumDistanceHeuristic e = new MinimumDistanceHeuristic();
        assertEquals(6 * COST, e.getValue(emptyGoals2), 0.1);
    }

    @org.junit.Test
    public void minimumDistance2() throws Exception {
        SokobanState.map = map1;
        LockAnalyzer.lockInitialize(emptyGoals1);
        MinimumDistanceHeuristic e = new MinimumDistanceHeuristic();
        assertEquals(3 * COST, e.getValue(emptyGoals1), 0.1);
    }

    @org.junit.Test
    public void manhattanDistanceObstacles() throws Exception {
        SokobanState.map = map1;
        LockAnalyzer.lockInitialize(emptyGoals1);
        Heuristic e = new ManhattanDistanceObstacles();
        assertEquals(4 * COST, e.getValue(emptyGoals1), 0.1);
    }

    @org.junit.Test
    public void manhattanDistanceObstacles2() throws Exception {
        SokobanState.map = map2;
        LockAnalyzer.lockInitialize(emptyGoals2);
        Heuristic e = new ManhattanDistanceObstacles();
        assertEquals(10 * COST, e.getValue(emptyGoals2), 0.1);
    }

    @org.junit.Test
    public void minimumDistancePlayer() throws Exception {
        SokobanState.map = map2;
        LockAnalyzer.lockInitialize(emptyGoals2);
        Heuristic e = new MinimumDistanceHeuristicPlayer();
        assertEquals(7 * COST, e.getValue(emptyGoals2), 0.1);
    }

    @org.junit.Test
    public void minimumDistance2Player() throws Exception {
        SokobanState.map = map1;
        LockAnalyzer.lockInitialize(emptyGoals1);
        Heuristic e = new MinimumDistanceHeuristicPlayer();
        assertEquals(9 * COST, e.getValue(emptyGoals1), 0.1);
    }

    @org.junit.Test
    public void manhattanDistanceObstaclesPlayer() throws Exception {
        SokobanState.map = map1;
        LockAnalyzer.lockInitialize(emptyGoals1);
        Heuristic e = new ManhattanDistanceObstaclesPlayer();
        assertEquals(10 * COST, e.getValue(emptyGoals1), 0.1);
    }

    @org.junit.Test
    public void manhattanDistanceObstacles2Player() throws Exception {
        SokobanState.map = map2;
        LockAnalyzer.lockInitialize(emptyGoals2);
        Heuristic e = new ManhattanDistanceObstaclesPlayer();
        assertEquals(11 * COST, e.getValue(emptyGoals2), 0.1);
    }

}
