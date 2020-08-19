package ar.edu.itba.sia.tp1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigParser {
    FileInputStream inputStream;

    // Variables

    private String strategy;
    private String heuristic;
    private int minDepth;
    private int step;
    private int maxDepth;
    private String mapPath;
    private boolean detect_lock;

    public boolean isDetect_lock() {
        return detect_lock;
    }

    public void getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "sokoban.properties";

            inputStream = new FileInputStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            // Default values
            prop.putIfAbsent("map", "easy.map");
            prop.putIfAbsent("strategy", "BFS");
            prop.putIfAbsent("heuristic", "EmptyGoalsHeuristic");
            prop.putIfAbsent("min_depth", "0");
            prop.putIfAbsent("step", "1");
            prop.putIfAbsent("max_depth", String.valueOf(Integer.MAX_VALUE));
            prop.putIfAbsent("detect_lock_states", "true");


            // get the property value and print it out
            mapPath = prop.getProperty("map");
            strategy = prop.getProperty("strategy");
            heuristic = prop.getProperty("heuristic");
            minDepth = Integer.valueOf(prop.getProperty("min_depth"));
            step = Integer.valueOf(prop.getProperty("step"));
            maxDepth = Integer.valueOf(prop.getProperty("max_depth"));
            detect_lock = Boolean.valueOf(prop.getProperty("detect_lock_states"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public int getMinDepth() {
        return minDepth;
    }

    public String getHeuristic() {
        return heuristic;
    }

    public String getStrategy() {
        return strategy;
    }

    public String getMapPath() {
        return mapPath;
    }

    public int getStep() {
        return step;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
