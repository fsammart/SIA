package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.Strategy;

public class ASTAR implements Strategy {
    Strategy DFS = new DFS();

    @Override
    public boolean needsExploring(Node n) {
        return DFS.needsExploring(n);
    }

    @Override
    public void visit(Node n) {
        DFS.visit(n);
    }

    @Override
    public int compare(Node o1, Node o2) {
        return Double.compare(o1.getCost() + o1.getHeuristicValue() ,o2.getCost() + o2.getHeuristicValue());
    }

    @Override
    public boolean needsHeuristic() {
        return true;
    }
}
