package ar.edu.itba.sia.tp1.api;

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
     * String Representation of state.
     *
     * @return The STRING representation of the state.
     */
    String toString();


}
