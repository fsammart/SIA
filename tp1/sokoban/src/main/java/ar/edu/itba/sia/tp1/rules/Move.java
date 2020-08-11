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

    public boolean canPush(SokobanState s, Point box){
        // 4 corners: UP RIGHT / UP LEFT / DOWN RIGHT / DOWN LEFT
        Point playerPosition = new Point(box.x -dx, box.y -dy);
        // check if player position is empty
        if(s.getElement(playerPosition) != SokobanState.EMPTY){
            // player position is considered empty
            return false;
        }
        Point targetPosition = box;
        Point targetNextPosition = new Point();
        targetNextPosition.setLocation(targetPosition.x + dx, targetPosition.y + dy);

        char targetNextObject = s.getElement(targetNextPosition);

        return targetNextObject == SokobanState.EMPTY;

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

        switch(targetObject){
            case SokobanState.WALL: {
                /* this is a wall -> not valid move*/
                return Optional.empty();
            }

            case SokobanState.BOX: {
                /* this is a box*/

                if(targetNextObject == SokobanState.BOX){
                    // this is a wall or 2 boxes that act as a wall
                    return Optional.empty();
                } else{
                    return Optional.of(push(sokobanState,playerPosition,targetPosition, targetNextPosition));
                }

            }
            case SokobanState.EMPTY: {
                /* blank space*/
                return Optional.of(move(sokobanState,playerPosition,targetPosition));
            }
        }

        // if we are here there is a problem with the board

        throw new IllegalStateException("Wrong board");
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
