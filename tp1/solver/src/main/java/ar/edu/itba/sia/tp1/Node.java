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
    private static Class modifierRuleClass;


    public Node(State state, int depth, Node parent, int cost, Rule birthRule) {
        this.state = state;
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
        this.birthRule = birthRule;
        this.heuristicValue = 0;

        heuristic.ifPresent(h -> {
            if(birthRule == null || (modifierRuleClass.isAssignableFrom(birthRule.getClass()))){
                this.heuristicValue = h.getValue(state);
            } else{
                this.heuristicValue = parent.heuristicValue;
            }
        } );
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
        h.ifPresent(p -> modifierRuleClass = p.getModifierRulesClass());
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
