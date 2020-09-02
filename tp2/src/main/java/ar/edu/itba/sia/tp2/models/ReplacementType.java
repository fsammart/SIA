package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.solver.Selection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum ReplacementType {
    FILL_ALL{
        @Override
        public List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                            List<Warrior> children, SelectionMethod rm1,
                                            SelectionMethod rm2,int sizeCombat, double percentage,
                                            int generation) {

            int n = prevGeneration.size();
            if(n < 300){
                System.out.println("hola");
            }
            List<Warrior> total = prevGeneration;
            total.addAll(children);
            Collections.shuffle(total);
            return Selection.select(rm1, rm2, percentage,total,n, sizeCombat, generation);

        }
    }, FILL_PARENT{
        @Override
        public List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                            List<Warrior> children, SelectionMethod rm1,
                                            SelectionMethod rm2, int sizeCombat, double percentage,
                                            int generation) {
            int n = prevGeneration.size();
            int k = children.size();
            if( n <= k){
                return Selection.select(rm1, rm2, percentage,children,n, sizeCombat, generation);
            }else{
                children.addAll(
                        Selection.select(rm1, rm2, percentage,prevGeneration,n - k, sizeCombat, generation)
                );
                return children;
            }
        }
    };

    public abstract List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                                 List<Warrior> children, SelectionMethod rm1,
                                                 SelectionMethod rm2, int sizeCombat, double percentage,
                                                 int generation);
}
