package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.solver.Selection;
import ar.edu.itba.sia.tp2.utils.Diversity;
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
                                            int generation, double diversityValue) {

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
                                            int generation, double diversityValue) {
            int n = prevGeneration.size();
            int k = children.size();
            List<Warrior> total = new ArrayList<>(n);
            if(prevGeneration.size()< 300){
                int a = 34;
            }
            if( n <= k){
                return Selection.select(rm1, rm2, percentage,children,n, sizeCombat, generation);
            }else{
                total.addAll(children);
                total.addAll(
                        Selection.select(rm1, rm2, percentage,prevGeneration,n - k, sizeCombat, generation)
                );
                return total;
            }
        }
    }, FILL_ALL_RANDOM{
        @Override
        public List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                            List<Warrior> children, SelectionMethod rm1,
                                            SelectionMethod rm2, int sizeCombat, double percentage,
                                            int generation, double diversityValue) {

            int n = prevGeneration.size();
            double percentageFix = diversityValue;
            percentageFix = ReplacementType.randomHeatProbability(percentageFix, generation);
            double d = Diversity.getDiversityPercentage(prevGeneration);
            int randomToAdd = 0;
            double delta = percentageFix - d;
            if(delta > 0){
                randomToAdd = (int) Math.ceil( (n) * delta);
            }


            List<Warrior> total = prevGeneration;
            total.addAll(children);

            List<Warrior> toReturn = Selection.select(rm1, rm2, percentage,total,n - randomToAdd, sizeCombat, generation);
            if(randomToAdd > 0) {
                IntStream.range(0, randomToAdd).forEach(idx -> {
                    toReturn.add(SRandom.getRandomWarrior());
                });
            }

            Collections.shuffle(toReturn);
            return toReturn;
        }
    }, FILL_ALL_RANDOM_CONSTANT{
        @Override
        public List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                            List<Warrior> children, SelectionMethod rm1,
                                            SelectionMethod rm2, int sizeCombat, double percentage,
                                            int generation, double diversityValue) {

            int n = prevGeneration.size();
            double percentageFix = diversityValue;
            double d = Diversity.getDiversityPercentage(prevGeneration);
            int randomToAdd = 0;
            double delta = percentageFix - d;
            if(delta > 0){
                randomToAdd = (int) Math.ceil( (n) * delta);
            }


            List<Warrior> total = prevGeneration;
            total.addAll(children);

            List<Warrior> toReturn = Selection.select(rm1, rm2, percentage,total,n - randomToAdd, sizeCombat, generation);
            if(randomToAdd > 0) {
                IntStream.range(0, randomToAdd).forEach(idx -> {
                    toReturn.add(SRandom.getRandomWarrior());
                });
            }

            Collections.shuffle(toReturn);
            return toReturn;
        }
    };

    public abstract List<Warrior> nextGeneration(List<Warrior> prevGeneration,
                                                 List<Warrior> children, SelectionMethod rm1,
                                                 SelectionMethod rm2, int sizeCombat, double percentage,
                                                 int generation, double diversityValue);

    private static double randomHeatProbability(double probability, int generation){
        return probability * Math.exp((-0.0025) * generation);
    }
}
