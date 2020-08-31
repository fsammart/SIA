package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.CrossoverType;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Crossover {

    public static List<Warrior> crossover(List<Warrior> parents, CrossoverType ct) {
        List<Warrior> children = new ArrayList<>(parents.size());
        Collections.shuffle(parents);
        for(int i = 0; i < parents.size()-1; i+=2){
            List<Warrior> aux = ct.crossOver(parents.get(i), parents.get(i +1));
            children.addAll(aux);
        }

        // TODO: check different if

        return children;
    }
}
