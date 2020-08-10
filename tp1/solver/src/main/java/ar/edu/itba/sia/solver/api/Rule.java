package ar.edu.itba.sia.solver.api;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Optional;

public interface Rule {
    /**
     * Applies rule to state.
     * @param state
     * @return new state after applying rule.
     */
    Optional<State> apply(State state);

    String getName();

}
