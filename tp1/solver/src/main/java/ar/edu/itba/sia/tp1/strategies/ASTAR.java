package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ASTAR extends RandomStrategy implements Strategy {

    HashMap<State, Integer> visited = new HashMap<>();

    @Override
    public boolean needsExploring(Node n) {
        if(visited.containsKey(n.getState()) && visited.get(n.getState()) <= n.getCost()){
            return false;
        }
        return true;
    }

    @Override
    public void visit(Node n) {
        visited.put(n.getState(),n.getCost());
    }

    @Override
    public int compare(Node o1, Node o2) {
        // TODO: optimización de f siempre mayor o igual que el padre.
        int compare = Double.compare(o1.getCost() + o1.getHeuristicValue() ,o2.getCost() + o2.getHeuristicValue());
        if(compare == 0){
            compare =  Double.compare(o1.getHeuristicValue(),o2.getHeuristicValue());
        }
        return compare==0 ? getRandomPriority() : compare;
    }

    @Override
    public boolean needsHeuristic() {
        return true;
    }

    @Override
    public List<Node> nextIteration() {
        return new LinkedList<>();
    }

    @Override
    public String toString() {
        return "A*";
    }
}
