package ar.edu.itba.sia.tp1.api;

import java.util.Optional;

public interface Rule {
    /**
     * Applies rule to state.
     * @param state
     * @return new state after applying rule.
     */
    Optional<State> apply(State state);

    int getCost();

    String getName();

}
