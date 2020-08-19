import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LockStateTest {

    char [][] mapNoDeadLock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', ' ', '$', ' ', ' ', ' ', ' ', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '.', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapCornerDeadlock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', ' ', '$', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', '$', ' ', ' ', ' ', ' ', '.', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapCornerGoalNoDeadlock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', ' ', '$', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '*', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapRowDeadLock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', '$', ' ', ' ', '#'},
            {'#', ' ', '$', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '.', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapColumnDeadLock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', '$', ' ', ' ', ' ', ' ', ' ', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '.', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapNoColumnDeadLock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', '$', ' ', ' ', ' ', ' ', ' ', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', '.', ' ', ' ', ' ', ' ', ' ', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapColumnWithGoalDeadLock = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', ' ', ' ', '#', '#', '#', ' ', ' ', ' ', '#'},
            {'#', '$', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', '$', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', '.', ' ', ' ', ' ', ' ', ' ', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    char [][] mapColumnAndRowLocks = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '@', '.', ' ', '#', '#', '#', ' ', '$', ' ', '#'},
            {'#', ' ', ' ', '$', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '#', '#', '#', '#', '#', '#', ' ', '#'},
            {'#', '$', ' ', '.', ' ', ' ', ' ', '#', '#', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    private SokobanState stateNoLock;
    private SokobanState stateCornerLock;
    private SokobanState stateRowLock;
    private SokobanState stateColumnLock;
    private SokobanState stateNoColumnLock;
    private SokobanState stateColumnWithGoalLock;
    private SokobanState stateCornerWithGoalNoLock;
    private SokobanState stateColumnAndRowLocks;


    @org.junit.Before
    public void setUp() throws Exception {
        stateNoLock = SokobanState.initialState(mapNoDeadLock).get();
        stateCornerLock = SokobanState.initialState(mapCornerDeadlock).get();
        stateRowLock = SokobanState.initialState(mapRowDeadLock).get();
        stateColumnLock = SokobanState.initialState(mapColumnDeadLock).get();
        stateNoColumnLock = SokobanState.initialState(mapNoColumnDeadLock).get();
        stateColumnWithGoalLock = SokobanState.initialState(mapColumnWithGoalDeadLock).get();
        stateCornerWithGoalNoLock = SokobanState.initialState(mapCornerGoalNoDeadlock).get();
        stateColumnAndRowLocks = SokobanState.initialState(mapColumnAndRowLocks).get();
    }

    @org.junit.Test
    public void hasNoLock() throws Exception {
        SokobanState.map = mapNoDeadLock;
        LockAnalyzer.lockInitialize(stateNoLock);
        assertFalse(LockAnalyzer.isLock(stateNoLock));
    }

    @org.junit.Test
    public void hasCornerDeadLock() throws Exception {
        SokobanState.map = mapCornerDeadlock;
        LockAnalyzer.lockInitialize(stateCornerLock);
        assertTrue(LockAnalyzer.isLock(stateCornerLock));
    }

    @org.junit.Test
    public void hasRowLock() throws Exception {
        SokobanState.map = mapRowDeadLock;
        LockAnalyzer.lockInitialize(stateRowLock);
        assertTrue(LockAnalyzer.isLock(stateRowLock));
    }

    @org.junit.Test
    public void hasColumnLock() throws Exception {
        SokobanState.map = mapColumnDeadLock;
        LockAnalyzer.lockInitialize(stateColumnLock);
        assertTrue(LockAnalyzer.isLock(stateColumnLock));
    }

    @org.junit.Test
    public void hasNoColumnLock() throws Exception {
        SokobanState.map = mapNoColumnDeadLock;
        LockAnalyzer.lockInitialize(stateNoColumnLock);
        assertFalse(LockAnalyzer.isLock(stateNoColumnLock));
    }

    @org.junit.Test
    public void hasColumnWithGoalLock() throws Exception {
        SokobanState.map = mapColumnWithGoalDeadLock;
        LockAnalyzer.lockInitialize(stateColumnWithGoalLock);
        assertTrue(LockAnalyzer.isLock(stateColumnWithGoalLock));
    }

    @org.junit.Test
    public void hasCornerWithGoalNoLock() throws Exception {
        SokobanState.map = mapCornerGoalNoDeadlock;
        LockAnalyzer.lockInitialize(stateCornerWithGoalNoLock);
        assertFalse(LockAnalyzer.isLock(stateCornerWithGoalNoLock));
    }

    @org.junit.Test
    public void lockColumnAndRowCheck() throws Exception {
        SokobanState.map = mapColumnAndRowLocks;
        LockAnalyzer.lockInitialize(stateColumnAndRowLocks);
        assertTrue(LockAnalyzer.isLock(stateColumnAndRowLocks));
    }
}
