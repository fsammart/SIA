package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import java.util.LinkedList;
import java.util.List;


public class EmptyGoalsHeuristic implements Heuristic {
    public double getValue(State s) {
        SokobanState ss = (SokobanState) s;
        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }

        return ss.getBoxes().stream().filter(b -> !ss.getGoals().contains(b)).count();
    }

    @Override
    public Class getModifierRulesClass() {
        return Push.class;
    }


}
