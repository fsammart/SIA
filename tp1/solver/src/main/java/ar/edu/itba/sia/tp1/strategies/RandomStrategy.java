package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.Random;

public abstract class RandomStrategy implements Strategy {

    boolean finished = false;

    private static Random r = new Random(System.currentTimeMillis());

    public int getRandomPriority(){
        return r.nextInt();
    }

    @Override
    public void finished(Node goalNode) {
        finished = true;
    }

    @Override
    public boolean hasFinished() {
        return finished;
    }
}
