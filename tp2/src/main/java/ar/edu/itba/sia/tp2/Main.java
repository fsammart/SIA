package ar.edu.itba.sia.tp2;

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

public class Main {

    private static PrintStream ps;
    private static PrintStream syso;

    public static void main(String[] args) throws IOException {
        File file = new File("results" + "/statistics" + System.currentTimeMillis() );
        if(file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        ps = new PrintStream(fos);
        syso = System.out;
        System.setOut(ps);
        System.out.println("Generation BestFit AvgFit WorstFit Diversity");
        

        ConfigParser cp = new ConfigParser();
        cp.getPropValues();

        InputFileParser ifp = new InputFileParser(cp.getInputFilePath());
        GeneticEngine ge = new GeneticEngine(cp, ifp, true, true);
        Warrior best;
        try {
            best = ge.run();
        }catch(Exception e){

            printStatistics(ge.getSummary(), ge.getOverallDiversity());
            return ;

        }
        System.out.println("BEST CHARACTER");
        System.out.println(best);



    }

    private static void printStatistics(List<DoubleSummaryStatistics> l, List<Double> overallDiveristy){
        System.setOut(ps);
        Integer i = 0;
        for(DoubleSummaryStatistics d: l){
            System.out.println(
                    String.format("%d %.4f %.4f %.4f %.4f", i, d.getMax(),d.getAverage(),d.getMin(), overallDiveristy.get(i))
            );
            i++;
        }
        System.setOut(syso);
    }
}
