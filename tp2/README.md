## Packaging Instructions

This project requires maven to compile.

```
cd /tp2
mvn install
mvn clean package
```

This will generate the .jar to run the project inside `/runner/target`

## To run

The following command will execute the solver with the specified arguments in sokoban.properties.
``
cd /tp2
mvn exec:java -Dexec.mainClass=ar.edu.itba.sia.tp2.Main 
``

## Results

Generation Evolution  will be saved under the directory `/results`.

## Configuration File

To execute the program with the desired parameters a properties file must be created with the name `sokoban.properties`.
An example can be found at `geneticsokoban.properties.example`


