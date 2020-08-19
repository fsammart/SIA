package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.LinkedList;
import java.util.List;

public class GlobalGreedy extends RandomStrategy implements Strategy {
    Strategy BFS = new BFS();
    @Override
    public boolean needsExploring(Node n) {
        return BFS.needsExploring(n);
    }

    @Override
    public void visit(Node n) {
        BFS.visit(n);
    }

    @Override
    public int compare(Node o1, Node o2) {
        int compare =  Double.compare(o1.getHeuristicValue() ,o2.getHeuristicValue());
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
        return "GolobalGreedy";
    }
}
