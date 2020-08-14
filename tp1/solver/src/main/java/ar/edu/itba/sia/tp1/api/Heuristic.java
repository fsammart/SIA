package ar.edu.itba.sia.tp1.api;

public interface Heuristic {

    /**
     * Returns heuristic value for state.
     * @param s
     * @return
     */
    public double getValue(State s);
}
