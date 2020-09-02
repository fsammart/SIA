package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.CrossoverParentSelection;
import ar.edu.itba.sia.tp2.models.CrossoverType;
import ar.edu.itba.sia.tp2.models.Warrior;
import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Crossover {

    public static List<Warrior> crossover(List<Warrior> parents, CrossoverType ct,
                                          double probability, CrossoverParentSelection cps) {
        List<Warrior> children = new ArrayList<>(parents.size());
        List<Map.Entry<Warrior,Warrior>> couples = cps.getCouples(parents);
        couples.forEach(e -> {
            List<Warrior> aux;
            if(SRandom.r.nextDouble() > probability){
                aux = List.of(e.getKey().clone(),e.getValue().clone());
            }else{
                aux = ct.crossOver(e.getKey(), e.getValue());
            }
            children.addAll(aux);
        });

        return children;
    }
}
