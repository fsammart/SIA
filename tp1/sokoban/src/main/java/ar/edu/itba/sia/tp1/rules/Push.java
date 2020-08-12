package ar.edu.itba.sia.tp1.rules;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;

import java.awt.*;
import java.util.Optional;
import java.util.Set;

public class Push implements Rule{
    private int dx;
    private int dy;
    private String name;
    private int cost;



    public Push(int dx, int dy, String name, int cost){
        this.dx = dx;
        this.dy = dy;
        this.name = name;
        this.cost = cost;
    }


    @Override
    public Optional<State> apply(State state) {
        SokobanState sokobanState = (SokobanState) state;
        Point playerPosition = sokobanState.getPlayerPosition();
        Point targetPosition = new Point();
        Point targetNextPosition = new Point();
        targetPosition.setLocation(playerPosition.x + dx, playerPosition.y + dy);
        targetNextPosition.setLocation(targetPosition.x + dx, targetPosition.y + dy);
        char targetObject = sokobanState.getElement(targetPosition);
        char targetNextObject = sokobanState.getElement(targetNextPosition);

        if(targetObject == sokobanState.BOX){
            if(targetNextObject == SokobanState.BOX || targetNextObject == SokobanState.WALL){
                // this is a wall or 2 boxes that act as a wall
                return Optional.empty();
            } else{
                return Optional.of(push(sokobanState,playerPosition,targetPosition, targetNextPosition));
            }
        }
        return Optional.empty();
    }

    @Override
    public int getCost() {
        return cost;
    }

    private SokobanState push(SokobanState s, Point playerPosition, Point boxPosition, Point nextPosition){
        SokobanState newState = (SokobanState) s.clone();
        Set<Point> boxes = newState.getBoxes();
        boxes.remove(boxPosition);
        boxes.add(nextPosition);
        newState.setPlayerPosition(boxPosition);

        return newState;
    }

    @Override
    public String getName() {
        return name;
    }
}
