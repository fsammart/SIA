package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.Strategy;

public class GlobalGreedy implements Strategy {
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
        int c = Integer.compare(o1.getDepth(), o2.getDepth());
        if(c != 0)
            return c;
        return Double.compare(o1.getHeuristicValue() ,o2.getHeuristicValue());
    }

    @Override
    public boolean needsHeuristic() {
        return true;
    }
}
