package ar.edu.itba.sia.sokoban.rules;

import ar.edu.itba.sia.sokoban.SokobanState;
import ar.edu.itba.sia.solver.api.Rule;
import ar.edu.itba.sia.solver.api.State;

import java.awt.Point;
import java.util.Optional;

public class Move implements Rule {
    private int dx;
    private int dy;


    @Override
    public boolean isApplicable(State state) {
        SokobanState s = (SokobanState) state;
        Point playerPosition = s.getPlayerPosition();
        if(Math.abs(dx) + Math.abs(dy) != 1){
            return false;
        } else if(playerPosition.getY() + dy >= s.getMap().length){

        }
        return false;
    }

    @Override
    public Optional<State> apply(State state) {
        SokobanState sokobanState = (SokobanState) state;
        Point playerPosition = sokobanState.getPlayerPosition();
        char targetPosition = sokobanState.getMap()[playerPosition.x + dx][playerPosition.y + dy];

        switch(targetPosition){
            case '#': {
                /* this is a wall -> not valid move*/

            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "generic move";
    }
}
