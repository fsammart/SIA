package ar.edu.itba.sia.tp1.rules;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;

import java.awt.Point;
import java.util.Optional;
import java.util.Set;

public class Move implements Rule {
    private int dx;
    private int dy;
    private String name;
    private int cost;



    public Move(int dx, int dy, String name, int cost){
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
        targetPosition.setLocation(playerPosition.x + dx, playerPosition.y + dy);
        char targetObject = sokobanState.getElement(targetPosition);

        if(targetObject == SokobanState.EMPTY){
            return Optional.of(move(sokobanState,playerPosition,targetPosition));
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

    private SokobanState move(SokobanState s, Point playerPosition, Point targetPosition){
        SokobanState newState = (SokobanState) s.clone();
        Set<Point> boxes = newState.getBoxes();
        newState.setPlayerPosition(targetPosition);
        return newState;
    }

    @Override
    public String getName() {
        return name;
    }
}
