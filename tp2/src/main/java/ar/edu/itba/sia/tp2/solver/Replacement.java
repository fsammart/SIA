package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.ReplacementType;
import ar.edu.itba.sia.tp2.models.SelectionMethod;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.ArrayList;
import java.util.List;

public class Replacement {

    public static List<Warrior> replace(List<Warrior> population, List<Warrior> children,
                                          int generation, ReplacementType rt,
                                          SelectionMethod rm1, SelectionMethod rm2, double percentage,
                                          int sizeCombat) {

        List<Warrior> l =  rt.nextGeneration(population,children,rm1,rm2,sizeCombat, percentage, generation);
        l.stream().forEach(w ->{
            if(Warrior.getBestWarrior() == null || w.getFitness()> Warrior.getBestWarrior().getFitness()){
                Warrior.setBestWarrior(w);
            }
        });

        return l;

    }
}
