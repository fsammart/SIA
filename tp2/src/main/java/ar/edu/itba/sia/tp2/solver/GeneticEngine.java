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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.w3c.dom.DOMImplementation;


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
    private List<Double> generations;
    private List<Double> maxFit;
    private List<Double> avgFit;
    private List<Double> minFit;
    private String[] axis = List.of("max_fit","avg_git","min_fit").toArray(new String[3]);
    XYChart chart;
    SwingWrapper<XYChart> sw ;
    private boolean graph;

    public GeneticEngine(ConfigParser cp, InputFileParser ifp, boolean graph) throws IOException {
        this.cp = cp;
        StopCriteria.cp = cp;
        this.summary = new ArrayList<>(2000);
        this.ifp = ifp;
        this.generation = 0;
        this.children = new ArrayList<>(cp.getPoolSize() + 2);
        this.selection = new ArrayList<>(cp.getPoolSize() + 2);
        this.prevGeneration = new ArrayList<>(cp.getPoolSize() + 2);
        this.graph = graph;
        SRandom.ifp = ifp;
        if(cp.getSeed() != 0){
            SRandom.seed = cp.getSeed();
        } else{
            SRandom.seed = System.currentTimeMillis();
        }

        generations = new ArrayList<>(100);
        maxFit = new ArrayList<>(100);
        avgFit = new ArrayList<>(100);
        minFit = new ArrayList<>(100);

        Warrior.setCoefficients(cp.getAttackCoefficient(), cp.getDefenseCoefficient());
        generations.add(0,Double.valueOf(0));
        maxFit.add(0,Double.valueOf(0));
        avgFit.add(0,Double.valueOf(0));
        minFit.add(0, Double.valueOf(0));

        // Create Chart
        chart = QuickChart.getChart("Fitness Evolution",
                "Generation", "Fitness","max_fit" , generations, maxFit);
        chart.addSeries("avg_fit", generations, avgFit);
        chart.addSeries("min_fit", generations, minFit);
        if(graph) {
            sw = new SwingWrapper<XYChart>(chart);
            sw.displayChart();
        }


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
        generations.add(generation, Double.valueOf(generation));
        maxFit.add(generation, d.getMax());
        avgFit.add(generation, d.getAverage());
        minFit.add(generation, d.getMin());

        if(graph) updateGraph();
    }

    private  void updateGraph(){
        chart.updateXYSeries("max_fit", generations, maxFit, null);
        chart.updateXYSeries("avg_fit", generations, avgFit, null);
        chart.updateXYSeries("min_fit", generations, minFit, null);

        sw.repaintChart();
    }

    public List<DoubleSummaryStatistics> getSummary() {
        return summary;
    }
}
