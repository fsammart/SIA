package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.MutationType;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.List;

public class Mutation {

    public static void mutate(List<Warrior> children, MutationType mt, int generation, double probability){
        children.forEach(c ->{
            mt.mutate(c,generation, probability);
        });
    }
}
