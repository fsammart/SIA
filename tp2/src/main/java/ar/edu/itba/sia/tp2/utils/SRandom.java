package ar.edu.itba.sia.tp2.utils;

import java.util.Random;

public class SRandom {

    static Random r = new Random(System.currentTimeMillis());

    static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }



}
