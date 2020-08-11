package ar.edu.itba.sia.tp1.utils;

import ar.edu.itba.sia.tp1.SokobanProblem;
import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.rules.Move;

import java.awt.*;
import java.util.Set;

public class LockAnalyzer {
    public static final boolean isLock(SokobanState s){
        SokobanState sokobanState = (SokobanState) s;
        Set<Point> boxes = sokobanState.getBoxes();
        return boxes.stream().filter(SokobanState.goals::contains).anyMatch(p -> boxLock(s,p));
    }

    private static final boolean boxLock(SokobanState s, Point boxPoint){
        // 4 corners: UP RIGHT / UP LEFT / DOWN RIGHT / DOWN LEFT
        /* If box has another box it acts as a wall*/
        return SokobanProblem.getRules().stream().anyMatch(m ->{
            return m.canPush(s, boxPoint);
        });

    }
}
