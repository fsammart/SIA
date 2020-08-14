package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.Strategy;
import ar.edu.itba.sia.tp1.heuristics.EmptyGoalsHeuristic;
import ar.edu.itba.sia.tp1.strategies.ASTAR;
import ar.edu.itba.sia.tp1.strategies.BFS;
import ar.edu.itba.sia.tp1.strategies.DFS;
import ar.edu.itba.sia.tp1.strategies.GlobalGreedy;

import java.util.Deque;
import java.util.Optional;

public class Main {

    public static void main(String args[]){

        char[][] map = null;
        String strategy = "ASTAR";

        try {
            map = MapParser.load("tp1/hard.map");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error while loading map");
            return ;
        }

        SokobanProblem sp = new SokobanProblem(map);

        Strategy dfs = getStrategy(strategy);

        Heuristic h = new EmptyGoalsHeuristic();


        Engine e = new Engine(sp, dfs, Optional.of(h));

        long init = System.currentTimeMillis();

        Deque<Rule> steps = e.solve().orElseThrow(() -> new IllegalStateException("Cannot solve"));

        long elapsed = System.currentTimeMillis() - init;

        System.out.println("Elapsed Time (ms)" + elapsed);

        steps.forEach(r -> System.out.println(r.getName()));
    }

    public static Strategy getStrategy(String strategy){
        switch(strategy){
            case "DFS": return new DFS();
            case "BFS": return new BFS();
            case "ASTAR": return new ASTAR();
            case "GREEDY": return new GlobalGreedy();
        }

        return null;
    }
}
