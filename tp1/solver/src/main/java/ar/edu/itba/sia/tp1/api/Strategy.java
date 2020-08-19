package ar.edu.itba.sia.tp1.api;

import ar.edu.itba.sia.tp1.Node;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


public interface Strategy extends Comparator<Node> {

    /**
     * Returns if Node n needs to be explored
     * @param n
     * @return
     */
    boolean needsExploring(Node n);

    void visit(Node n);

    boolean needsHeuristic();

    List<Node> nextIteration();

    void finished(Node goalNode);

    boolean hasFinished();

}