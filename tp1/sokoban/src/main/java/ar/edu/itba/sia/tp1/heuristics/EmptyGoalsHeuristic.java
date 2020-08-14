package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class EmptyGoalsHeuristic implements Heuristic {
    @Override
    public double getValue(State s) {
        SokobanState ss = (SokobanState) s;
        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }
        return ss.getBoxes().stream().filter(b -> !ss.getGoals().contains(b)).count();
    }

}
