package ar.edu.itba.sia.tp1.utils;

import ar.edu.itba.sia.tp1.SokobanProblem;
import ar.edu.itba.sia.tp1.SokobanState;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MapAnalyzer {

    public static int[][] calculateReachable(char [][] map, Set<Point> goals){
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

    private static Map<Point,int[][]> goalsDistSegments = null;


    public static Map<Point,int[][]> calculateDistances(char [][] map, Set<Point> goals){
        Map<Point,int[][]> goalsDistMap = new HashMap<>();
        for(Point g: goals){
            goalsDistMap.put(g, new int[map.length][map[0].length]);
        }

        int[][] distanceMap = new int[map.length][map[0].length];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j ++){
                if (map[i][j] == '#'){
                    distanceMap[i][j] = -2; // WALL
                } else{
                    distanceMap[i][j] = -1; //cannot reach
                }
                for(Point g: goals){
                    goalsDistMap.get(g)[i][j] = distanceMap[i][j];
                }
            }
        }
        for(Point g: goals) {
            Deque<Point> opened = new LinkedList<Point>();
            opened.add(g);
            distanceMap = goalsDistMap.get(g);
            int currentDistance;
            while (!opened.isEmpty()) {
                Point p = opened.poll();
                if ( g.equals(p)) {
                    distanceMap[p.x][p.y] = 0;
                }
                currentDistance = distanceMap[p.x][p.y] + 1;
                if (p.x + 1 < map.length
                        && distanceMap[p.x + 1][p.y] != -2
                        && (distanceMap[p.x + 1][p.y] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                    distanceMap[p.x + 1][p.y] = currentDistance;
                    opened.add(new Point(p.x + 1, p.y));
                }
                if (p.x - 1 >= 0
                        && distanceMap[p.x - 1][p.y] != -2
                        && (distanceMap[p.x - 1][p.y] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                    distanceMap[p.x - 1][p.y] = currentDistance;
                    opened.add(new Point(p.x - 1, p.y));
                }
                if (p.y + 1 < map[0].length
                        && distanceMap[p.x][p.y + 1] != -2
                        && (distanceMap[p.x][p.y + 1] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                    distanceMap[p.x][p.y + 1] = currentDistance;
                    opened.add(new Point(p.x, p.y + 1));
                }
                if (p.y - 1 >= 0
                        && distanceMap[p.x][p.y - 1] != -2
                        && (distanceMap[p.x][p.y - 1] == -1 || distanceMap[p.x + 1][p.y] > currentDistance)) {
                    distanceMap[p.x][p.y - 1] = currentDistance;
                    opened.add(new Point(p.x, p.y - 1));
                }

            }
        }
        return goalsDistMap;
    }

    public static int getPlayerMinDistanceToBox(SokobanState ss){
        Set<Point> goals = ss.getGoals();
        // boxes in wrong position
        List<Point> boxes = ss.getBoxes().stream().filter(b -> !goals.contains(b)).collect(Collectors.toList());
        if(boxes.isEmpty()){
            // goal state
            return 0;
        }
        Point playerPosition = ss.getPlayerPosition();
        int minDistance = -1;
        for(Point b: boxes){
            int aux = manhattanDistance(playerPosition,b);
            if(minDistance == -1 ||aux < minDistance ){
                minDistance = aux;
            }
        }

        //player needs to be one block outside box to push
        return minDistance - 1;
    }

    private static int manhattanDistance(Point a, Point b){
        return Math.abs(a.x-b.x) + Math.abs(a.y-b.y);
    }
}
