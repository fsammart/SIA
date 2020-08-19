package ar.edu.itba.sia.tp1.heuristics;

import ar.edu.itba.sia.tp1.SokobanState;
import ar.edu.itba.sia.tp1.api.Heuristic;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.rules.Push;
import ar.edu.itba.sia.tp1.utils.LockAnalyzer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManhattanDistanceObstacles implements Heuristic {
    private int[][] distMap = null;

    public static int[][] calculateDistances(char [][] map, Set<Point> goals){
        int [][] distanceMap = new int[map.length][map[0].length];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j ++){
                if (map[i][j] == '#'){
                    distanceMap[i][j] = -2; // WALL
                } else{
                    distanceMap[i][j] = -1; //cannot reach
                }
            }
        }

        Deque<Point> opened = new LinkedList<Point>();
        opened.addAll(goals);
        int currentDistance;
        while(!opened.isEmpty()){
            Point p = opened.poll();
            if(distanceMap[p.x][p.y] == -1 || goals.contains(p)){
                distanceMap[p.x][p.y] = 0;
            }
            currentDistance = distanceMap[p.x][p.y] + 1;
            if(p.x + 1 < map.length
                    && distanceMap[p.x + 1][p.y] != -2
                    && (distanceMap[p.x + 1][p.y] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                distanceMap[p.x + 1][p.y] = currentDistance;
                opened.add(new Point(p.x + 1, p.y));
            }
            if(p.x - 1 >= 0
                    && distanceMap[p.x - 1][p.y] != -2
                    && (distanceMap[p.x - 1][p.y] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                distanceMap[p.x - 1][p.y] = currentDistance;
                opened.add(new Point(p.x - 1, p.y));
            }
            if(p.y + 1 < map[0].length
                    && distanceMap[p.x][p.y + 1] != -2
                    && (distanceMap[p.x][p.y + 1] == -1|| distanceMap[p.x + 1][p.y] > currentDistance)) {
                distanceMap[p.x][p.y + 1] = currentDistance;
                opened.add(new Point(p.x, p.y + 1));
            }
            if(p.y -1 >= 0
                    && distanceMap[p.x][p.y - 1] != -2
                    && (distanceMap[p.x][p.y -1] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                distanceMap[p.x][p.y - 1] = currentDistance;
                opened.add(new Point(p.x , p.y -1));
            }

        }
        return distanceMap;
    }
    @Override
    public double getValue(State s) {
        SokobanState ss = (SokobanState) s;
        if(distMap == null){
            distMap = calculateDistances(ss.getMap(), ss.getGoals());
        }
        if(LockAnalyzer.isLock(ss)){
            return Double.MAX_VALUE;
        }

        int minDistance = 0;

        for(Point p: ss.getBoxes()){
            minDistance += distMap[p.x][p.y];
        }

        return minDistance*100;
    }

    @Override
    public Class getModifierRulesClass() {
        return Push.class;
    }

    @Override
    public String toString() {
        return "ManhattanDistanceObstacles";
    }
}
