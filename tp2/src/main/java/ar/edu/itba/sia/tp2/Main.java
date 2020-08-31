package ar.edu.itba.sia.tp2;

import ar.edu.itba.sia.tp2.solver.GeneticEngine;
import ar.edu.itba.sia.tp2.utils.ConfigParser;
import ar.edu.itba.sia.tp2.utils.InputFileParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ConfigParser cp = new ConfigParser();
        cp.getPropValues();

        InputFileParser ifp = new InputFileParser();

        GeneticEngine ge = new GeneticEngine(cp, ifp);

        ge.run();

    }
}
