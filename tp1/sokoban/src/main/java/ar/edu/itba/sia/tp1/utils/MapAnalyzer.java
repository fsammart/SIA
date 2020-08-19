package ar.edu.itba.sia.tp1.utils;

import java.awt.*;
import java.util.*;

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
}
