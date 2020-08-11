package ar.edu.itba.sia.tp1;

import java.io.BufferedReader;
import java.io.FileReader;

public class MapParser {

    static char[][] load(String filename) throws Exception {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Integer mapCols = Integer.parseInt(bufferedReader.readLine());
        Integer mapRows = Integer.parseInt(bufferedReader.readLine());

        char[][] map = new char[mapRows][mapCols];

        for(int row = 0; row < mapRows; row++) {
            char[] line = bufferedReader.readLine().toCharArray();

            for(int col = 0; col < mapCols; col++) {
                map[row][col] = line[col];
            }
        }

        return map;
    }

}
