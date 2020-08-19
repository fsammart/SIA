package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.Random;

public abstract class RandomStrategy implements Strategy {

    private static Random r = new Random(System.currentTimeMillis());

    public int getRandomPriority(){
        return r.nextInt();
    }
}
