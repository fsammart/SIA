package ar.edu.itba.sia.solver.api;

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
     * returns all possible states from a given state, applying all available rules
     * for the state.
     * Check if each rule has a cost assigned, that way we can only return Rule.
     * @param s
     * @return List with all possible descendant states.
     */
    Map<State, Map.Entry<Rule, Integer>> getDescendants(State s);
}
