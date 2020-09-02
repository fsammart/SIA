package ar.edu.itba.sia.tp2;

import ar.edu.itba.sia.tp2.models.CrossoverParentSelection;
import ar.edu.itba.sia.tp2.models.Warrior;
import ar.edu.itba.sia.tp2.solver.GeneticEngine;
import ar.edu.itba.sia.tp2.utils.ConfigParser;
import ar.edu.itba.sia.tp2.utils.InputFileParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class Analysis {
    private static PrintStream ps;
    private static PrintStream syso;

    public static void main(String[] args) throws IOException {

        File file = new File("results" + "/statistics" + System.currentTimeMillis() + ".tsv");
        if(file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        ps = new PrintStream(fos);
        syso = System.out;
        System.setOut(ps);
        System.out.println("" +
                "pool_size SM1 SM2 alpha K RM1 RM2 beta mutation_type mutation_probability mutation_heat " +
                "crossover_type crossover_probability couple_selection stop_criteria " +
                "generation best_fit avg_fit worst_fit");
        System.setOut(syso);

        ConfigParser cp = new ConfigParser();
        cp.getPropValues();

        InputFileParser ifp = new InputFileParser();

        for(CrossoverParentSelection cps : CrossoverParentSelection.values()){
            cp.setCrossoverParentSelection(cps);
            GeneticEngine ge = new GeneticEngine(cp, ifp);
            ge.run();
            printStatistics(ge.getSummary(), cp);
        }

    }

    private static void printStatistics(List<DoubleSummaryStatistics> l, ConfigParser cp){
        System.setOut(ps);
        Integer i = 0;
        for(DoubleSummaryStatistics d: l){
            System.out.println(
                    String.format("%d %s %s %.3f %d %s %s %.3f" +
                            " %s %.4f %s %s %.4f %s %s %d %.4f %.4f %.4f",cp.getPoolSize(),
                            cp.getSelectionMethod1().toString(),
                            cp.getSelectionMethod2().toString(),
                            cp.getSelectionMethod1Percentage(),
                            cp.getSelectionQuantity(),
                            cp.getReplacementMethod1(),
                            cp.getReplacementMethod2(),
                            cp.getReplacementMethod1Percentage(),
                            cp.getMutationType(),
                            cp.getMutationProbability(),
                            cp.isMutationHeat(),
                            cp.getCrossoverType(),
                            cp.getCrossoverProbability(),
                            cp.getCrossoverParentSelection(),
                            cp.getStopCriteria(),
                            i++, d.getMax(),d.getAverage(),d.getMin())
            );
        }
        System.setOut(syso);
    }
}
