package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.solver.Selection;
import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public enum ReplacementType {
    FILL_ALL{
        @Override
        public List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                            List<Warrior> children, SelectionMethod rm1,
                                            SelectionMethod rm2,int sizeCombat, double percentage,
                                            int generation) {

            int n = prevGeneration.size();

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
    }, FILL_ALL_RANDOM{
        @Override
        public List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                            List<Warrior> children, SelectionMethod rm1,
                                            SelectionMethod rm2, int sizeCombat, double percentage,
                                            int generation) {

            int n = prevGeneration.size();

            List<Warrior> total = new ArrayList<>(n + 2);
            IntStream.range(0, n).forEach(idx -> {
                total.add(SRandom.getRandomWarrior());
            });

            total.addAll(children);
            Collections.shuffle(total);
            return Selection.select(rm1, rm2, percentage,total,n, sizeCombat, generation);
        }
    };

    public abstract List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                                 List<Warrior> children, SelectionMethod rm1,
                                                 SelectionMethod rm2, int sizeCombat, double percentage,
                                                 int generation);
}
