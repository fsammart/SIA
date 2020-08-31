package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public enum CrossoverType {
    ANNULAR{
        @Override
        public List<Warrior> crossOver(Warrior p1, Warrior p2) {
            return annularCrossover(p1, p2);
        }
    }, UNIFORM{
        @Override
        public List<Warrior> crossOver(Warrior p1, Warrior p2) {
            return uniformCrossover(p1,p2);
        }
    }, SINGLE_POINT{
        @Override
        public List<Warrior> crossOver(Warrior p1, Warrior p2) {
            return singlePointCrossover(p1, p2);
        }
    }, TWO_POINT{
        @Override
        public List<Warrior> crossOver(Warrior p1, Warrior p2) {
            return twoPointCrossover(p1, p2);
        }
    };

    public abstract List<Warrior> crossOver(Warrior p1, Warrior p2);


    private static List<Warrior> singlePointCrossover(Warrior p1, Warrior p2) {
        Integer firstGene = SRandom.r.nextInt(Warrior.getGeneNumber());
        return crossover(p1,p2,firstGene, Warrior.getGeneNumber());

    }

    private static List<Warrior> twoPointCrossover(Warrior p1, Warrior p2) {
        Integer firstGene = SRandom.r.nextInt(Warrior.getGeneNumber());
        Integer secondGene = SRandom.r.nextInt(Warrior.getGeneNumber());
        if (firstGene > secondGene) {
            return crossover(p1,p2,secondGene, firstGene);
        } else {
            return  crossover(p1,p2, firstGene, secondGene);
        }

    }

    private static List<Warrior> crossover(Warrior p1, Warrior p2, Integer firstGene, Integer secondGene) {

        List<Warrior> children = new ArrayList<Warrior>(List.of(p1.clone(), p2.clone()));
        for(int i = firstGene; i < secondGene; i++) {
            Gene gene = Gene.values()[i];
            changeGene(gene,p1,p2,children.get(0),children.get(1));
        }
        return children;
    }


    private static List<Warrior> uniformCrossover(Warrior p1, Warrior p2) {
        List<Warrior> children =  new ArrayList<Warrior>(List.of(p1.clone(), p2.clone()));
        Arrays.stream(Gene.values()).forEach(gene ->{
            double p = SRandom.r.nextDouble();
            if(p < 0.5) {
                changeGene(gene,p1,p2,children.get(0),children.get(1));
            }
        });
        return children;
    }

    private static List<Warrior> annularCrossover(Warrior p1, Warrior p2) {
        List<Warrior> children =  new ArrayList<Warrior>(List.of(p1.clone(), p2.clone()));
        int geneIndex = SRandom.r.nextInt(Warrior.getGeneNumber());

        IntStream.range(0, SRandom.r.nextInt(4)).forEach(rightMoves -> {
            Gene gene = Gene.values()[(geneIndex+rightMoves)%Warrior.getGeneNumber()];
            changeGene(gene,p1,p2,children.get(0),children.get(1));
        });

        return children;
    }

    private static void changeGene(Gene gene,Warrior p1, Warrior p2, Warrior c1, Warrior c2) {
        c2.setToken(gene, p1.getToken(gene));
        c1.setToken(gene, p2.getToken(gene));
    }
}
