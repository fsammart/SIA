package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;
import ar.edu.itba.sia.tp1.heuristics.*;
import ar.edu.itba.sia.tp1.strategies.*;

import java.io.*;
import java.util.*;

public class Statistics {

    private static int minDepth;
    private static int step;
    private static int maxDepth;

    public static void main(String args[]) throws FileNotFoundException {

        char[][] map = null;
        String mapPath = "maps/map5.map";

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
                Arrays.asList("ASTAR","IDASTAR","GREEDY","IDGREEDY"));
        Strategy strategy;
        Heuristic h;
        boolean isDetectLock;
        Engine e;
        Optional<Deque<Map.Entry<State,Rule>>> result;
        PrintStream ps = openFile();
        for(int timeout = 10; timeout < 100000; timeout+=10){
            h = new ManhattanDistanceObstacles();
            strategy = new IDGREEDY(5,70,timeout);
            e = new Engine(sp, strategy, Optional.ofNullable(h), true);
            result = e.solve();
            printStatistics(ps, mapPath, strategy, h, result, e.getMetrics(),
                    String.valueOf(minDepth), String.valueOf(step), String.valueOf(maxDepth),true,timeout);
        }
        /*
        for(String s : strategies){
            for(int i = 0; i < REPEAT; i++) {
                h = new ManhattanDistanceObstacles();
                strategy = getStrategy(s, h);
                if(strategy.needsHeuristic()) {
                    isDetectLock = true;
                    e = new Engine(sp, strategy, Optional.ofNullable(h), isDetectLock);
                    result = e.solve();
                    printStatistics(ps, mapPath, strategy, h, result, e.getMetrics(),
                            String.valueOf(minDepth), String.valueOf(step), String.valueOf(maxDepth), isDetectLock);

                    h = new ManhattanDistanceObstaclesPlayer();
                    strategy = getStrategy(s, h);
                    e = new Engine(sp, strategy, Optional.ofNullable(h), isDetectLock);
                    result = e.solve();
                    printStatistics(ps, mapPath, strategy, h, result, e.getMetrics(),
                            String.valueOf(minDepth), String.valueOf(step), String.valueOf(maxDepth), isDetectLock);
                }
                /*
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

        }*/

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
            case "IDGREEDY": {
                return new IDGREEDY(5,74,100);
            }
        }

        return null;
    }

    private static Heuristic getHeuristic(String heuristic){
        switch (heuristic.toUpperCase()){
            case "EMPTYGOALS": return new EmptyGoalsHeuristic();
            case "MINIMUMDISTANCE": return new MinimumDistanceHeuristic();
            case "MANHATTANOBSTACLES": return new ManhattanDistanceObstacles();
            case "MINMATCHING": return new MinMatchingHeuristic();
            case "EMPTYGOALSP": return new EmptyGoalsHeuristicPlayer();
            case "MINIMUMDISTANCEP": return new MinimumDistanceHeuristicPlayer();
            case "MANHATTANOBSTACLESP": return new ManhattanDistanceObstaclesPlayer();
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
                                     Optional<Deque<Map.Entry<State,Rule>>> result, Metrics m,
                                        String minDepth, String step, String maxDepth, boolean checkLock, int timeout) throws FileNotFoundException {
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

        System.out.print(m.getElapsedTime() + ",");
        System.out.print(timeout);

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

        System.out.println("Map,Strategy,Heuristic,checkLock,minDepth,Step,MaxDepth,Solved,Depth,Cost,ExpandedNodes,BoundaryNodes,Time (ms), timeout");

        System.setOut(stdout);
        return ps;
    }
}
