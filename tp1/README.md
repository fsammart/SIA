## Packaging Instructions

This project requires maven to compile.

```
cd /tp1
mvn install
mvn clean package
```

This will generate the .jar to run the project inside `/runner/target`

## To run

The following command will execute the solver with the specified arguments in sokoban.properties.
``
cd /tp1
mvn exec:java -Dexec.mainClass=ar.edu.itba.sia.tp1.Main -f runner
``

## Results

Results will be saved under the directory `/results` with the name "result + SystemMilis".

## Configuration File

To execute the program with the desired parameters a properties file must be created with the name `sokoban.properties`.
An example can be found at `sokoban.properties.example`

## Map Encoding

In the folder `/maps` there are several maps to try. Encoding is specified here: http://www.game-sokoban.com/index.php?mode=level_info&ulid=2084&view=general

Walls are encoded with '#'
Blank spaces with ' ' (space)
Blocks over goals with '*'
Player Position over Empty Space with '@'
Player Position over Goal with '+'


