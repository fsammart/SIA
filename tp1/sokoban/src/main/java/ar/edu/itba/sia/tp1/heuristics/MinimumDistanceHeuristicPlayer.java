package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanProblem;
import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.utils.MapAnalyzer;

public class MinimumDistanceHeuristicPlayer extends MinimumDistanceHeuristic {
    @Override
    public double getValue(State s) {
        double ret = super.getValue(s);
        if(ret < Double.MAX_VALUE){
            return ret + MapAnalyzer.getPlayerMinDistanceToBox((SokobanState) s) * SokobanProblem.COST;
        }
        return ret;
    }

    @Override
    public Class getModifierRulesClass() {
        return Rule.class;
    }

    @Override
    public String toString() {
        return "MinimumDistancePlayer";
    }
}
