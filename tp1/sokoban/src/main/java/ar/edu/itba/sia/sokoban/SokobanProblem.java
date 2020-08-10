package ar.edu.itba.sia.sokoban;

import ar.edu.itba.sia.solver.api.Problem;
import ar.edu.itba.sia.solver.api.Rule;
import ar.edu.itba.sia.solver.api.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SokobanProblem implements Problem{


    private State initialState;
    private List<Rule> rules;


    public SokobanProblem(char[][] map){
        initialState = SokobanState.initialState(map).orElseThrow(() -> {
            return new IllegalArgumentException("Wrong Initial State");
        });


    }

    public State getInitialState() {
        return initialState;
    }

    @Override
    public boolean isGoal(State s) {
        SokobanState sokobanState = (SokobanState) s;
        char[][] map = sokobanState.getMap();
        // TODO: rewrite
        // The state is goal if there aren't unfilled goals (no '.' or '+' in the matrix)
        for(int row = 0; row < map.length; row++) {
            for(int col = 0; col < map[0].length; col++) {
                if(map[row][col] == '.' || map[row][col] == '+') {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Map<State, Map.Entry<Rule, Integer>> getDescendants(State s) {

        return null;
    }



}
