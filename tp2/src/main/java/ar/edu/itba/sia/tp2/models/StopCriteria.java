package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.utils.ConfigParser;

import java.util.Collections;
import java.util.List;

public enum StopCriteria {

    TIME{
        @Override
        public boolean hasFinished(List<Warrior> prevGeneration, List<Warrior> currentGeneration,
                                   int generationNumber) {
            if(initialTime == -1){
                initialTime = System.currentTimeMillis();
            }

            if(System.currentTimeMillis() - initialTime >= cp.getTime()){
                return true;
            }
            return false;
        }
    }, GENERATION_COUNT{
        @Override
        public boolean hasFinished(List<Warrior> prevGeneration, List<Warrior> currentGeneration,
                                   int generationNumber) {
            return generationNumber >= cp.getMaxGenerations();
        }
    }, ADMISSIBLE_SOLUTION{
        @Override
        public boolean hasFinished(List<Warrior> prevGeneration, List<Warrior> currentGeneration,
                                   int generationNumber) {
            if(Math.abs(Collections.max(currentGeneration).getFitness() - cp.getFitnessOptimum()) < 0.01)
                return true;
            return false;
        }
    }, CONTENT{
        @Override
        public boolean hasFinished(List<Warrior> prevGeneration, List<Warrior> currentGeneration,
                                   int generationNumber) {
            if(generationNumber == 0)
                return false;
            double prevMaxRating = Collections.max(prevGeneration).getFitness();
            double maxRating = Collections.max(currentGeneration).getFitness();

            if(Double.compare(maxRating, prevMaxRating) == 0)
                repeatedGenerationsContent++;
            else {
                repeatedGenerationsContent = 0;
                return false;
            }
            return repeatedGenerationsContent >= cp.getBestCharacterRepetitionLimit();
        }
    }, STRUCTURE{
        @Override
        public boolean hasFinished(List<Warrior> prevGeneration, List<Warrior> currentGeneration,
                                   int generationNumber) {
            if (generationNumber == 0)
                return false;
            double repeatedCharacters = 0;

            for(Warrior w: currentGeneration){
                repeatedCharacters += prevGeneration.parallelStream()
                        .anyMatch(character -> w.isApproximateEqualTo(character))? 1: 0;
            }


            if (repeatedCharacters / currentGeneration.size() > cp.getPoolRepetitionPercentageLimit()) {
                repeatedGenerationsStructure++;
                if (repeatedGenerationsStructure >= cp.getMaxRepeatedPercentageGenerations()) {
                    return true;
                }
                return false;
            }
            repeatedGenerationsStructure = 0;
            return false;
        }
    }, COMBINED{
        @Override
        public boolean hasFinished(List<Warrior> prevGeneration, List<Warrior> currentGeneration,
                                   int generationNumber) {
        return STRUCTURE.hasFinished(prevGeneration, currentGeneration, generationNumber)
                && CONTENT.hasFinished(prevGeneration, currentGeneration, generationNumber);
        }
    };

    public abstract boolean hasFinished(List<Warrior> prevGenetation, List<Warrior> currentGeneration,
                                        int generationNumber);
    public static ConfigParser cp;
    private static Integer repeatedGenerationsContent = 0;
    private static Integer repeatedGenerationsStructure = 0;
    private static long initialTime = -1;
}
