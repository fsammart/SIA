package ar.edu.itba.sia.tp1.api;

import ar.edu.itba.sia.tp1.Node;

import java.util.List;
import java.util.Map;

public interface Problem {

    /**
     * Provides the initial state for the GPS to start from.
     *
     * @return The initial state of the problem to be solved.
     */
    State getInitialState();

    /**
     * checks whether state is solution for the problem or not.
     * @param s
     * @return
     */
    boolean isGoal(State s);

    /**
     * returns all possible states from a given node state, applying all available rules
     * for the state.
     * Check if each rule has a cost assigned, that way we can only return Rule.
     * @param s
     * @return List with all possible descendant states.
     */
    List<Node> getDescendants(Node n);


    /**
     * Checks is state is in lock condition.
     * Generation rule is added for compute reduce
     * @param s
     * @return
     */
    boolean isLock(State s, Rule generationRule);
}
