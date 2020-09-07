package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.MutationType;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.List;

public class Mutation {

    public static void mutate(List<Warrior> children, MutationType mt,
                              int generation, double probability, boolean isMutationHeat){
        final double proba;
        if(isMutationHeat) {
            proba = mutationHeatProbability(probability, generation);
        } else{
            proba = probability;
        }
        children.forEach(c ->{
            mt.mutate(c,generation, proba);
        });
    }

    private static double mutationHeatProbability(double probability, int generation){
        return probability * Math.exp((-0.0025) * generation);
    }
}
