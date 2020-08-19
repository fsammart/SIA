package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.Problem;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.Strategy;
import ar.edu.itba.sia.tp1.heuristics.EmptyGoalsHeuristic;
import ar.edu.itba.sia.tp1.heuristics.ManhattanDistanceObstacles;
import ar.edu.itba.sia.tp1.heuristics.MinMatchingHeuristic;
import ar.edu.itba.sia.tp1.heuristics.MinimumDistanceHeuristic;
import ar.edu.itba.sia.tp1.strategies.*;

import java.io.IOException;
import java.util.Deque;
import java.util.Optional;

public class Main {

    private static ConfigParser c;

    public static void main(String args[]) {

         c = new ConfigParser();
         try {
             c.getPropValues();
         } catch(IOException e){
             System.out.println("Problems while reading properties file");
         }

        char[][] map = null;

        try {
            map = MapParser.load(c.getMapPath());
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error while loading map");
            return ;
        }

        SokobanProblem sp = new SokobanProblem(map);
        Heuristic h = getHeuristic(c.getHeuristic());
        Strategy strategy = getStrategy(c.getStrategy(), h, sp);

        Engine e = new Engine(sp, strategy, Optional.ofNullable(h), c.isDetect_lock());


        Optional<Deque<Rule>> result = e.solve();

        printResults(strategy, h, result, e.getMetrics(), e.getGoalNode());

    }

    public static Strategy getStrategy(String strategy, Heuristic h, Problem p){
        switch(strategy.toUpperCase()){
            case "DFS": return new DFS();
            case "BFS": return new BFS();
            case "ASTAR": return new ASTAR();
            case "GREEDY": return new GlobalGreedy();
            case "IDDFS": {
                return new IDDFS(c.getMax() ,c.getMin(),c.getStep());
            }
            case "IDGREEDY": {
                return new IDGREEDY(c.getStep() * SokobanProblem.COST,
                        c.getMin() * SokobanProblem.COST,c.getTimeout());
            }
            case "IDASTAR": {
                return new IDASTAR(h);
            }
            default: return new DFS();
        }
    }

    private static Heuristic getHeuristic(String heuristic){
        switch (heuristic.toUpperCase()){
            case "EMPTYGOALS": return new EmptyGoalsHeuristic();
            case "KM": return new MinMatchingHeuristic();
            case "MINIMUMDISTANCE": return new MinimumDistanceHeuristic();
            case "MANHATTANOBSTACLES": return new ManhattanDistanceObstacles();
            case "MINMATCHING": return new MinMatchingHeuristic();
            default: return new EmptyGoalsHeuristic();
        }
    }

    private static void printParams(Strategy s, Heuristic h){
        System.out.println("Running With:");
        System.out.println("Map -> " + c.getMapPath());
        System.out.println("Strategy -> " + c.getStrategy());
        if(h != null && s.needsHeuristic()){
            System.out.println("Heuristic -> " + c.getHeuristic());
        }
        if(h != null && "IDDFS".equals(h.toString())){
            System.out.println("Minimum Depth -> " + c.getMin());
            System.out.println("Step -> " + c.getStep());
            System.out.println("Maximum Depth-> " + c.getMax());
        }
        if(h != null && "IDGREEDY".equals(h.toString())){
            System.out.println("Minimum Cost -> " + c.getMin() * SokobanProblem.COST);
            System.out.println("Step -> " + c.getStep()* SokobanProblem.COST);
            System.out.println("Timeout -> " + c.getTimeout());
        }
    }

    private static void printResults(Strategy s, Heuristic h,
                                     Optional<Deque<Rule>> result, Metrics m, Node goalNode){
        printParams(s,h);
        System.out.println("Elapsed Time (ms) -> " + m.getElapsedTime());
        if(!result.isPresent()){
            System.out.println("Can´t Solve");


            return;
        }
        Deque<Rule> steps = result.get();

        System.out.println("\nSolved!!\n");
        if(!s.needsHeuristic()) System.out.println("Detect Locks (BFS,DFS,IDDFS): " + c.isDetect_lock());
        System.out.println("Depth -> " + m.getDepth());
        System.out.println("Cost -> " + m.getCost());
        System.out.println("Expanded Nodes -> " + m.getExpandedNodes());
        System.out.println("Boundary Nodes -> " + m.getBoundaryNodes());
        System.out.println("");
        System.out.println("Solution: " );

        steps.forEach(r -> System.out.println(r.getName()));
    }
}
