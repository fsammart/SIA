package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BFS implements Strategy {

    Set<State> visited = new HashSet<>();

    @Override
    public boolean needsExploring(Node n) {
        State currentState = n.getState();

        if(visited.contains(currentState)){
            //already visited with lower cost
            return false;
        }
        return true;
    }

    @Override
    public void visit(Node n) {
        visited.add(n.getState());
    }

    @Override
    public int compare(Node o1, Node o2) {
        return Integer.compare(o1.getDepth(),o2.getDepth());
    }

    @Override
    public boolean needsHeuristic() {
        return false;
    }
}
