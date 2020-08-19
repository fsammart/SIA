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
    private int min;
    private int step;
    private int max;
    private String mapPath;
    private boolean detect_lock;
    private long timeout;

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
            prop.putIfAbsent("min", "0");
            prop.putIfAbsent("step", "1");
            prop.putIfAbsent("max", String.valueOf(Integer.MAX_VALUE));
            prop.putIfAbsent("detect_lock_states", "true");
            prop.putIfAbsent("timeout", String.valueOf(Integer.MAX_VALUE));


            // get the property value and print it out
            mapPath = prop.getProperty("map");
            strategy = prop.getProperty("strategy");
            heuristic = prop.getProperty("heuristic");
            min = Integer.valueOf(prop.getProperty("min"));
            step = Integer.valueOf(prop.getProperty("step"));
            max = Integer.valueOf(prop.getProperty("max"));
            detect_lock = Boolean.valueOf(prop.getProperty("detect_lock_states"));
            timeout = Long.valueOf(prop.getProperty("timeout"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public int getMin() {
        return min;
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

    public int getMax() {
        return max;
    }

    public long getTimeout() {
        return timeout;
    }
}
