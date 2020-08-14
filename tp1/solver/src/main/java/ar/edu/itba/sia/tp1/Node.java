package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;

import java.util.Optional;

public class Node {
    private final State state;
    private final int depth;
    private final Node parent;
    private final int cost;
    private final Rule birthRule;
    private  double heuristicValue;
    private static Optional<Heuristic> heuristic;


    public Node(State state, int depth, Node parent, int cost, Rule birthRule) {
        this.state = state;
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
        this.birthRule = birthRule;
        heuristic.ifPresent(h -> this.heuristicValue = h.getValue(state));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return state.equals(node.state);
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public static void setHeuristic(Optional<Heuristic> h){
        heuristic = h;
    }

    public State getState() {
        return state;
    }

    public int getDepth() {
        return depth;
    }

    public Node getParent() {
        return parent;
    }

    public int getCost() {
        return cost;
    }

    public Rule getBirthRule() {
        return birthRule;
    }
}
