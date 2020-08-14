package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DFS implements Strategy {


    Map<State,Integer> visited = new HashMap<>();


    @Override
    public boolean needsExploring(Node n) {
        State currentState = n.getState();

        if(visited.containsKey(currentState)
                && visited.get(currentState) <= n.getCost()){
            //already visited with lower cost
            return false;
        }
        return true;
    }

    @Override
    public int compare(Node o1, Node o2) {
        return Integer.compare(o2.getDepth(), o1.getDepth());
    }

    @Override
    public void visit(Node n) {
        visited.put(n.getState(),n.getCost());
    }

    @Override
    public boolean needsHeuristic() {
        return false;
    }
}
