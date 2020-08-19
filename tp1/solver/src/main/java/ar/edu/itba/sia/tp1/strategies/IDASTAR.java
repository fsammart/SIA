package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class IDASTAR extends RandomStrategy implements Strategy {

    // visited with depth
    HashMap<State, Integer> visited = new HashMap<>();
    PriorityQueue<Node> toOpenLater;
    Strategy s = new ASTAR();

    private int currentMaxDepth;

    public IDASTAR(Heuristic h){
        this.toOpenLater = new PriorityQueue<>(new ASTAR());
        this.currentMaxDepth = -1;
    }

    @Override
    public boolean needsExploring(Node n) {
        if(currentMaxDepth == -1){
            this.currentMaxDepth = (int) n.getHeuristicValue() + n.getCost(); // cost should be 0
        }
        if(visited.containsKey(n.getState()) && visited.get(n.getState()) <= n.getCost()){
            return false;
        }

        if(n.getDepth() > currentMaxDepth){
            // we are in the next level
            toOpenLater.add(n);
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
        return s.compare(o1,o2);
    }

    @Override
    public boolean needsHeuristic() {
        return true;
    }

    @Override
    public List<Node> nextIteration() {
        if(toOpenLater.isEmpty()){
            return new LinkedList<>();
        }
        Node n = toOpenLater.peek();
        currentMaxDepth = n.getCost() + (int) n.getHeuristicValue();
        List<Node> l = new LinkedList<>(toOpenLater);
        toOpenLater = new PriorityQueue<>(new ASTAR());
        return  l;
    }

    @Override
    public String toString() {
        return "IDASTAR";
    }
}
