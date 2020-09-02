package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.SelectionMethod;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.List;

public class Selection {

    public static List<Warrior> select(SelectionMethod sm1, SelectionMethod sm2, double percentage,
                                         List<Warrior> warriors, int size, int combat_size, int generation){
        int size1 = (int) Math.round(size * percentage);
        int size2 = size - size1;

        List<Warrior> selected = sm1.select(warriors,size1,combat_size,generation);
        List<Warrior> selected2 = sm2.select(warriors,size2,combat_size,generation);
        if(selected2.size() == 0){
            System.out.println("HOLIS");
        }
        selected.addAll(selected2);
        return selected;
    }
}
