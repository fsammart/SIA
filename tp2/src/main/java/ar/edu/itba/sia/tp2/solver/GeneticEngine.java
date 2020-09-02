package ar.edu.itba.sia.tp2.solver;

import ar.edu.itba.sia.tp2.models.Gene;
import ar.edu.itba.sia.tp2.models.StopCriteria;
import ar.edu.itba.sia.tp2.models.Warrior;
import ar.edu.itba.sia.tp2.utils.ConfigParser;
import ar.edu.itba.sia.tp2.utils.InputFileParser;
import ar.edu.itba.sia.tp2.utils.SRandom;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

public class GeneticEngine {

    private List<Warrior> population;
    private List<Warrior> children;
    private List<Warrior> selection;
    private List<Warrior> prevGeneration;

    private ConfigParser cp;
    private InputFileParser ifp;
    private PrintStream ps;
    private PrintStream syso;
    //state variables

    private int generation;
    private List<DoubleSummaryStatistics> summary;

    public GeneticEngine(ConfigParser cp, InputFileParser ifp) throws IOException {
        this.cp = cp;
        StopCriteria.cp = cp;
        this.summary = new ArrayList<>(2000);
        this.ifp = ifp;
        this.generation = 0;
        this.children = new ArrayList<>(cp.getPoolSize() + 2);
        this.selection = new ArrayList<>(cp.getPoolSize() + 2);
        this.prevGeneration = new ArrayList<>(cp.getPoolSize() + 2);
        SRandom.ifp = ifp;
        if(cp.getSeed() != 0){
            SRandom.seed = cp.getSeed();
        } else{
            SRandom.seed = System.currentTimeMillis();
        }

        Warrior.setCoefficients(cp.getAttackCoefficient(), cp.getDefenseCoefficient());


    }

    public Warrior run(){
        initialPopulation();

        while(!hasFinished()){
            children.clear();

            // 1.SELECTION
            // TODO: check selection
            selection = Selection.select(cp.getSelectionMethod1(), cp.getSelectionMethod2(),
                    cp.getSelectionMethod1Percentage(), population,
                    cp.getSelectionQuantity(),cp.getTournamentCombatants(), generation);

            // 2.CROSS_OVER

            children = Crossover.crossover(selection, cp.getCrossoverType(),
                    cp.getCrossoverProbability(), cp.getCrossoverParentSelection());

            // 3.CHILDREN MUTATIONS - In-Site operation

            Mutation.mutate(children, cp.getMutationType() , generation, cp.getMutationProbability(), cp.isMutationHeat());

            // 4.REPLACEMENT
            this.prevGeneration.clear();
            this.prevGeneration.addAll(population);

            this.population = Replacement.replace(this.population,children,generation, cp.getReplacementType(),
                    cp.getReplacementMethod1(), cp.getSelectionMethod2(), cp.getReplacementMethod1Percentage(),
                    cp.getTournamentCombatants());


            generation++;

            printStatistics();

        }

        return Warrior.getBestWarrior();
    }

    // TODO: other end criteria
    private boolean hasFinished() {
       return cp.getStopCriteria().hasFinished(prevGeneration, population, generation);
    }

    private void initialPopulation(){
        this.population = new ArrayList<>(cp.getPoolSize() + 2);
        IntStream.range(0, cp.getPoolSize()).forEach(idx -> {
                this.population.add(SRandom.getRandomWarrior());
        });
    }

    private void printStatistics(){
        System.setOut(ps);

        DoubleSummaryStatistics d = population.stream().mapToDouble(Warrior::getFitness).summaryStatistics();
        summary.add(d);
    }

    public List<DoubleSummaryStatistics> getSummary() {
        return summary;
    }
}
