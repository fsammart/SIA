package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Problem;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Move;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;


public class SokobanProblem implements Problem{


    private static State initialState;
    private static List<Rule> rules;
    public static int COST = 100;


    public SokobanProblem(char[][] map){
        initialState = SokobanState.initialState(map).orElseThrow(() -> {
            return new IllegalArgumentException("Wrong Initial State");
        });

        rules = new LinkedList<>();
        rules.add(new Move(0,1, "right",COST));
        rules.add(new Move(0,-1, "left",COST));
        rules.add(new Move(1,0, "down",COST));
        rules.add(new Move(-1,0, "up",COST));

        rules.add(new Push(0,1, "push right",COST));
        rules.add(new Push(0,-1, "push left",COST));
        rules.add(new Push(1,0, "push down",COST));
        rules.add(new Push(-1,0, "push up",COST));

        LockAnalyzer.lockInitialize((SokobanState) initialState);

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
    public boolean isLock(State s, Rule r) {
        if(r != null && r.getClass().equals(Move.class)){
            // If previous movement is not push then dont analyze.
            return false;
        }
        return LockAnalyzer.isLock((SokobanState) s);
    }

    @Override
    public int getCostAmplifier() {
        return SokobanProblem.COST;
    }

    @Override
    public List<Node> getDescendants(Node n) {
        List<Node> movements = new LinkedList<>();

        for(Rule r : rules){
            r.apply(n.getState()).ifPresent(
                    newState -> movements.add(
                            new Node(newState,n.getDepth() + 1,n, n.getCost() + r.getCost(),r)));
        }
        return movements;
    }

    public static List<Rule> getRules() {
        return rules;
    }
}
