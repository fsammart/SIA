package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Problem;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Move;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;


public class SokobanProblem implements Problem{


    private static State initialState;
    private static List<Move> rules;


    public SokobanProblem(char[][] map){
        initialState = SokobanState.initialState(map).orElseThrow(() -> {
            return new IllegalArgumentException("Wrong Initial State");
        });

        rules = new LinkedList<>();
        rules.add(new Move(0,1, "right",1));
        rules.add(new Move(0,-1, "left",1));
        rules.add(new Move(1,0, "down",1));
        rules.add(new Move(-1,0, "up",1));

    }

    public State getInitialState() {
        return initialState;
    }

    @Override
    public boolean isGoal(State s) {
        SokobanState sokobanState = (SokobanState) s;
        char[][] map = sokobanState.getMap();
        Set<Point> goals = SokobanState.goals;
        if(sokobanState.getBoxes().containsAll(goals)){
            return true;
        }

        return false;
    }

    @Override
    public boolean isLock(State s) {
        return LockAnalyzer.isLock((SokobanState) s);
    }

    @Override
    public Map<Rule, State> getDescendants(State s) {
        Map<Rule,State> movements = new HashMap<>();
        for(Move r : rules){
            r.apply(s).ifPresent(newState -> movements.put(r,newState));
        }
        return movements;
    }

    public static List<Move> getRules() {
        return rules;
    }
}
