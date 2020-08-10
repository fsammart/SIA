package ar.edu.itba.sia.solver;

import ar.edu.itba.sia.solver.api.Rule;
import ar.edu.itba.sia.solver.api.State;

public class Node {
    private final State state;
    private final int depth;
    private final Node parent;
    private final int cost;
    private final Rule birthRule;


    public Node(State state, int depth, Node parent, int cost, Rule birthRule) {
        this.state = state;
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
        this.birthRule = birthRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return state.equals(node.state);
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
