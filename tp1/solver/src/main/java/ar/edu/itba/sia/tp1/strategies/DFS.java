package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.*;

public class DFS extends RandomStrategy implements Strategy {

    Strategy BFS = new BFS();

    @Override
    public boolean needsExploring(Node n) {
        return BFS.needsExploring(n);
    }

    @Override
    public int compare(Node o1, Node o2) {
        int compare = Integer.compare(o2.getDepth(), o1.getDepth());
        return  compare==0 ? getRandomPriority() : compare;
    }

    @Override
    public void visit(Node n) {
        BFS.visit(n);
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
        return "DFS";
    }
}
