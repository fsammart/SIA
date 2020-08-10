package ar.edu.itba.sia.solver.api;

public interface State {
    /**
     * Must implement hashcode and equals, do not leave Object implementation.
     */

    /**
     *
     * @param state
     * @return true if 2 states are problem-equal.
     */
    boolean equals(Object state);

    /**
     *
     * @return problem relevant hashcode of state.
     */
    int hashcode();


    /**
     * String Representation of state.
     *
     * @return The STRING representation of the state.
     */
    String toString();
}
