package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.Strategy;
import ar.edu.itba.sia.tp1.heuristics.EmptyGoalsHeuristic;
import ar.edu.itba.sia.tp1.heuristics.ManhattanDistanceObstacles;
import ar.edu.itba.sia.tp1.heuristics.MinMatchingHeuristic;
import ar.edu.itba.sia.tp1.heuristics.MinimumDistanceHeuristic;
import ar.edu.itba.sia.tp1.strategies.*;

import java.io.*;
import java.util.*;

public class Statistics {

    private static int minDepth;
    private static int step;
    private static int maxDepth;

    public static void main(String args[]) throws FileNotFoundException {

        char[][] map = null;
        String mapPath = "easy.map";

        minDepth = 5;
        step = 2;
        maxDepth = Integer.MAX_VALUE;

        int REPEAT = 5;


        try {
            map = MapParser.load(mapPath);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error while loading map");
            return ;
        }

        SokobanProblem sp = new SokobanProblem(map);
        List<String> strategies =  new LinkedList<String>(
                Arrays.asList("BFS","DFS","IDDFS","ASTAR","IDASTAR","GREEDY"));
        Strategy strategy;
        Heuristic h;
        boolean isDetectLock;
        Engine e;
        Optional<Deque<Rule>> result;
        PrintStream ps = openFile();
        for(String s : strategies){
            for(int i = 0; i < REPEAT; i++) {
                isDetectLock = true;
                h = new ManhattanDistanceObstacles();
                strategy = getStrategy(s, h);
                e = new Engine(sp, strategy, Optional.ofNullable(h), isDetectLock);
                result = e.solve();
                printStatistics(ps, mapPath, strategy, h, result, e.getMetrics(),
                        String.valueOf(minDepth), String.valueOf(step), String.valueOf(maxDepth), isDetectLock);
                if(!strategy.needsHeuristic()) {
                    strategy = getStrategy(s, h);
                    isDetectLock = false;
                    h = new ManhattanDistanceObstacles();
                    e = new Engine(sp, strategy, Optional.ofNullable(h), isDetectLock);
                    result = e.solve();
                    printStatistics(ps, mapPath, strategy, h, result, e.getMetrics(),
                            String.valueOf(minDepth), String.valueOf(step), String.valueOf(maxDepth), isDetectLock);
                }
            }
        }

    }

    public static Strategy getStrategy(String strategy, Heuristic h){
        switch(strategy){
            case "DFS": return new DFS();
            case "BFS": return new BFS();
            case "ASTAR": return new ASTAR();
            case "GREEDY": return new GlobalGreedy();
            case "IDDFS": {
                return new IDDFS(maxDepth,minDepth,step);
            }
            case "IDASTAR": {
                return new IDASTAR(h);
            }
        }

        return null;
    }

    private static Heuristic getHeuristic(String heuristic){
        switch (heuristic.toUpperCase()){
            case "EMPTYGOALS": return new EmptyGoalsHeuristic();
            case "KM": return new MinMatchingHeuristic();
            case "MINIMUMDISTANCE": return new MinimumDistanceHeuristic();
            case "MANHATTANOBSTACLES": return new ManhattanDistanceObstacles();
            default: return new EmptyGoalsHeuristic();
        }
    }

    private static void printParams(Strategy s, Heuristic h,
                                    String mapPath, String minDepth, String step, String maxDepth, boolean checkLock){
        System.out.print(mapPath + ",");
        System.out.print(s.toString() + ",");
        System.out.print(h + ",");
        System.out.print(checkLock + ",");
        System.out.print(minDepth + ",");
        System.out.print(step + ",");
        System.out.print(maxDepth + ",");

    }

    private static void printStatistics(PrintStream ps, String mapPath, Strategy strategy, Heuristic h,
                                     Optional<Deque<Rule>> result, Metrics m,
                                        String minDepth, String step, String maxDepth, boolean checkLock) throws FileNotFoundException {
        PrintStream stdout = System.out;
        System.setOut(ps);
        printParams(strategy, h, mapPath, minDepth, step, maxDepth, checkLock);

        if(!result.isPresent()){
            System.out.print("false" + ",");
            System.out.print(",");
            System.out.print(",");
            System.out.print(",");
            System.out.print(",");
            System.out.print(m.getElapsedTime());
            System.out.println("");
            System.setOut(stdout);

            return;
        }

        System.out.print("true" + ",");
        System.out.print(m.getDepth() + ",");
        System.out.print(m.getCost() + ",");
        System.out.print(m.getExpandedNodes() + ",");
        System.out.print(m.getBoundaryNodes() + "," );

        System.out.print(m.getElapsedTime());
        System.out.println("");
        System.setOut(stdout);

    }

    private static PrintStream openFile() throws FileNotFoundException {
        final String filename = "statistics" + System.currentTimeMillis();
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        PrintStream stdout = System.out;
        System.setOut(ps);

        System.out.println("Map,Strategy,Heuristic,checkLock,minDepth,Step,MaxDepth,Solved,Depth,Cost,ExpandedNodes,BoundaryNodes,Time (ms)");

        System.setOut(stdout);
        return ps;
    }
}
