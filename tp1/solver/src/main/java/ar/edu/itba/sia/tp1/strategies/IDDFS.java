package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IDDFS extends RandomStrategy implements Strategy {

    // visited with depth
    HashMap<State, Integer> visited = new HashMap<>();
    List<Node> toOpenLater = new LinkedList<>();

    private int maxDepth;
    private int currentMaxDepth;
    private int step;
    private int repeated;

    public IDDFS(int maxDepth, int startDepth, int step){
        this.maxDepth = maxDepth;
        this.currentMaxDepth = startDepth;
        this.step = step;
        this.repeated = 0;
    }

    @Override
    public boolean needsExploring(Node n) {
        if(visited.containsKey(n.getState()) && visited.get(n.getState()) <= n.getDepth()){
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
        visited.put(n.getState(),n.getDepth());
    }

    @Override
    public int compare(Node o1, Node o2) {
        int compare = Integer.compare(o2.getDepth(), o1.getDepth());
        return compare==0 ? getRandomPriority() : compare;

    }

    @Override
    public boolean needsHeuristic() {
        return false;
    }

    @Override
    public List<Node> nextIteration() {
        if(currentMaxDepth  + step > maxDepth){
            return new LinkedList<>();
        }
        currentMaxDepth += step;
        List<Node> l = toOpenLater;
        toOpenLater = new LinkedList<>();
        return l;
    }

    @Override
    public String toString() {
        return "IDDFS";
    }
}
