package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum MutationType {
    ONE_GENE{
        @Override
        public void mutate(Warrior r, int generation, double probability) {
            if(SRandom.r.nextDouble() < probability) {
                mutateGene(r, Gene.values()[SRandom.r.nextInt(Warrior.getGeneNumber())]);
            }
        }
    }, LIMITED{
        @Override
        public void mutate(Warrior r, int generation, double probability) {
            if(SRandom.r.nextDouble() < probability) {
                List<Gene> genes = Arrays.asList(Gene.values().clone());
                Collections.shuffle(genes);
                int m = SRandom.r.nextInt(Warrior.getGeneNumber());
                List<Gene> toMutate = genes.subList(0,m);
                toMutate.forEach(g -> {
                    ONE_GENE.mutate(r, generation, probability);
                });
            }
        }
    }, UNIFORM{
        @Override
        public void mutate(Warrior r, int generation, double probability) {
            Arrays.stream(Gene.values()).forEach(g ->{
                ONE_GENE.mutate(r,generation, probability);
            });
        }
    }, COMPLETE{
        @Override
        public void mutate(Warrior r, int generation, double probability) {
            if(SRandom.r.nextDouble() < probability) {
                Arrays.stream(Gene.values()).forEach(g -> {
                    ONE_GENE.mutate(r, generation, 1);
                });
            }
        }
    };

    public abstract void mutate(Warrior r, int generation, double probability);


    private static void mutateGene(Warrior w, Gene type) {
        if(type == Gene.HEIGHT) {
            w.setHeight(1.3 + SRandom.r.nextDouble() * 0.7 );
        } else {
            Token randomItem = SRandom.getRandomToken(type);
            w.setToken(type, randomItem);
        }
    }
}
