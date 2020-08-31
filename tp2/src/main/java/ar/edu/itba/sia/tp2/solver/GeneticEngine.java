package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.Gene;
import ar.edu.itba.sia.tp2.models.Warrior;
import ar.edu.itba.sia.tp2.utils.ConfigParser;
import ar.edu.itba.sia.tp2.utils.InputFileParser;
import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class GeneticEngine {

    private List<Warrior> population;
    private List<Warrior> children;
    private List<Warrior> selection;
    private List<Warrior> prevGeneration;

    private ConfigParser cp;
    private InputFileParser ifp;

    //state variables

    private int generation;

    public GeneticEngine(ConfigParser cp, InputFileParser ifp){
        this.cp = cp;
        this.ifp = ifp;
        this.generation = 0;
        this.children = new ArrayList<>(cp.getPoolSize() + 2);
        this.selection = new ArrayList<>(cp.getPoolSize() + 2);
        this.prevGeneration = new ArrayList<>(cp.getPoolSize() + 2);
        SRandom.ifp = ifp;
        Warrior.setCoefficients(cp.getAttackCoefficient(), cp.getDefenseCoefficient(),
                cp.getStrengthCoefficient(), cp.getAgilityCoefficient(),
                cp.getExpertiseCoefficient(), cp.getResistanceCoefficient(),
                cp.getVitalityCoefficient());

    }

    public Warrior run(){
        initialPopulation();

        while(hasFinished()){
            children.clear();

            // 1.SELECTION
            // TODO: check selection
            selection = Selection.select(cp.getSelectionMethod1(), cp.getSelectionMethod2(),
                    cp.getSelectionMethod1Percentage(), population,
                    cp.getSelectionQuantity(),cp.getTournamentCombatants(), generation);

            // 2.CROSS_OVER

            children = Crossover.crossover(selection, cp.getCrossoverType());

            // 3.CHILDREN MUTATIONS - In-Site operation

            Mutation.mutate(children, cp.getMutationType() , generation, cp.getMutationProbability());

            // 4.REPLACEMENT
            this.prevGeneration.clear();
            this.prevGeneration.addAll(population);

            this.population = Replacement.replace(this.population,children,generation, cp.getReplacementType(),
                    cp.getReplacementMethod1(), cp.getSelectionMethod2(), cp.getReplacementMethod1Percentage(),
                    cp.getTournamentCombatants());


            generation++;

            printStatistics();

        }

        return null;
    }

    // TODO: other end criteria
    private boolean hasFinished() {
        return generation <= cp.getMaxGenerations();
    }

    private void initialPopulation(){
        this.population = new ArrayList<>(cp.getPoolSize() + 2);
        IntStream.range(0, cp.getPoolSize()).forEach(idx -> {
                this.population.add(SRandom.getRandomWarrior());
        });
    }

    private void printStatistics(){
        population.sort(Comparator.naturalOrder());
        System.out.println("Generation: " + generation);
        for(int i = 0; i < 5; i++){
            System.out.println(population.get(population.size() - 1 - i));
        }
    }
}
