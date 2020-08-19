package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanProblem;
import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;
import ar.edu.itba.sia.tp1.utils.MapAnalyzer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManhattanDistanceObstacles implements Heuristic {
    private int[][] distMap = null;

    @Override
    public double getValue(State s) {
        SokobanState ss = (SokobanState) s;
        if(distMap == null){
            distMap = MapAnalyzer.calculateReachable(ss.getMap(), ss.getGoals());
        }
        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }

        int minDistance = 0;

        for(Point p: ss.getBoxes()){
            minDistance += distMap[p.x][p.y];
        }

        return minDistance * SokobanProblem.COST;
    }

    @Override
    public Class getModifierRulesClass() {
        return Push.class;
    }

    @Override
    public String toString() {
        return "ManhattanDistanceObstacles";
    }
}
