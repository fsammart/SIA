package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MinimumDistanceHeuristic implements Heuristic {
    /**
     * Returns minimum Distance from block to goal and multiplies then by amount of empty goals.
     * @param s
     * @return
     */
    @Override
    public double getValue(State s) {
        SokobanState ss = (SokobanState) s;
        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }

        List<Point> l  =  ss.getBoxes().stream()
                .filter(b -> !ss.getGoals().contains(b)).collect(Collectors.toList());

        if(l.isEmpty()){
            // this is a goal state.
            return 0;
        }
        int minDistance = 0;

        for(Point p: l){
            for(Point g: ss.getGoals()){
                int distance = Math.abs(g.x - p.x) + Math.abs(g.y - p.y);
                if(minDistance == 0 || distance < minDistance){
                    minDistance = distance;
                }
            }
        }

        return minDistance * l.size();
    }

    @Override
    public Class getModifierRulesClass() {
        return Push.class;
    }
}
