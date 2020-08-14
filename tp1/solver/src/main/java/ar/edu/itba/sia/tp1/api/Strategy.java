package ar.edu.itba.sia.tp1.api;

import ar.edu.itba.sia.tp1.Node;

import java.util.Comparator;
import java.util.Map;


public interface Strategy extends Comparator<Node> {

    /**
     * Returns if Node n needs to be explored
     * @param n
     * @return
     */
    public boolean needsExploring(Node n);

    public void visit(Node n);

    public boolean needsHeuristic();


}