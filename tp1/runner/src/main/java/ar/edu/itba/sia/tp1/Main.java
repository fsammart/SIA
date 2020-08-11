package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Rule;

import java.util.Deque;

public class Main {

    public static void main(String args[]){

        char[][] map = null;
        try {
            map = MapParser.load("baby.map");
        }catch(Exception e){
            System.out.println("Error while loading map");
            return ;
        }

        SokobanProblem sp = new SokobanProblem(map);

        Engine e = new Engine(sp);

        Deque<Rule> steps = e.solve().orElseThrow(() -> new IllegalStateException("Cannot solve"));

        steps.forEach(r -> System.out.println(r.getName()));
    }
}
