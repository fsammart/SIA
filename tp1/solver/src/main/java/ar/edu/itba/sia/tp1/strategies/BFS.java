package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.*;

public class BFS extends RandomStrategy implements Strategy {

    Set<State> visited = new HashSet<>();

    @Override
    public boolean needsExploring(Node n) {
        State currentState = n.getState();

        if(visited.contains(currentState)){
            //already visited
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
        int compare =Integer.compare(o1.getDepth(),o2.getDepth());

        return  compare==0 ? getRandomPriority() : compare;
    }

    @Override
    public boolean needsHeuristic() {
        return false;
    }

    @Override
    public List<Node> nextIteration() {
        return new LinkedList<>();
    }
    @Override
    public String toString() {
        return "BFS";
    }
}
