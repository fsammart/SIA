package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Rule;

import java.util.Deque;

public class Main {

    public static void main(String args[]){

        char[][] map = null;
        try {
            map = MapParser.load("easy.map");
        }catch(Exception e){
            System.out.println("Error while loading map");
            return ;
        }

        SokobanProblem sp = new SokobanProblem(map);

        Engine e = new Engine(sp);

        long init = System.currentTimeMillis();

        Deque<Rule> steps = e.solve().orElseThrow(() -> new IllegalStateException("Cannot solve"));

        long elapsed = System.currentTimeMillis() - init;

        System.out.println("Elapsed Time (ms)" + elapsed);

        steps.forEach(r -> System.out.println(r.getName()));
    }
}
