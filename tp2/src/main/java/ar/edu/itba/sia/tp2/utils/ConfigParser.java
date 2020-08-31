package ar.edu.itba.sia.tp2.utils;

import ar.edu.itba.sia.tp2.models.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigParser {

    FileInputStream inputStream;

    public static final int NUMBER_OF_GENES = 6;

    private Integer poolSize;

    private SelectionMethod selectionMethod1;
    private SelectionMethod selectionMethod2;
    private Float selectionMethod1Percentage;
    private Integer selectionQuantity;
    private Integer tournamentCombatants;

    private StopCriteria stopCriteria;
    private Integer maxGenerations;
    private Integer bestCharacterRepetitionLimit;
    private Integer maxRepeatedPercentageGenerations;
    private Double poolRepetitionPercentageLimit;
    private Double fitnessOptimum;

    private MutationType mutationType;
    private Double mutationProbability;

    private CrossoverType crossoverType;
    //private CrossoverPair crossoverPair;
    private Double crossoverProbability;

    private ReplacementType replacementType;
    private SelectionMethod replacementMethod1;
    private SelectionMethod replacementMethod2;
    private Float replacementMethod1Percentage;

    private Double attackCoefficient;
    private Double defenseCoefficient;
    private Double strengthCoefficient;
    private Double agilityCoefficient;
    private Double expertiseCoefficient;
    private Double resistanceCoefficient;
    private Double vitalityCoefficient;
    private int recolectionPeriod;
    private long time;


    public void getPropValues() throws IOException {

        try {
            Properties p = new Properties();
            String propFileName = "genetic.properties";

            inputStream = new FileInputStream(propFileName);

            if (inputStream != null) {
                p.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            attackCoefficient = Double.parseDouble(p.getProperty("ATTACK_COEFFICIENT", "0.9"));
            defenseCoefficient = Double.parseDouble(p.getProperty("DEFENSE_COEFFICIENT", "0.1"));
            strengthCoefficient = Double.parseDouble(p.getProperty("STRENGTH_COEFFICIENT", "0.8"));
            agilityCoefficient = Double.parseDouble(p.getProperty("AGILITY_COEFFICIENT", "1.1"));
            expertiseCoefficient = Double.parseDouble(p.getProperty("EXPERTISE_COEFFICIENT", "1.1"));
            resistanceCoefficient = Double.parseDouble(p.getProperty("RESISTANCE_COEFFICIENT", "0.9"));
            vitalityCoefficient = Double.parseDouble(p.getProperty("VITALITY_COEFFICIENT", "0.7"));

            mutationProbability = Double.parseDouble(p.getProperty("MUTATION_PROBABILITY"));
            mutationType = MutationType.valueOf(p.getProperty("MUTATION_TYPE"));

            selectionMethod1 = SelectionMethod.valueOf(p.getProperty("SELECTION_METHOD_1"));
            selectionMethod2 = SelectionMethod.valueOf(p.getProperty("SELECTION_METHOD_2"));
            selectionMethod1Percentage = Float.parseFloat(p.getProperty("SELECTION_METHOD_1_PERCENTAGE"));
            selectionQuantity = Integer.parseInt(p.getProperty("SELECTION_QUANTITY"));

            if(selectionMethod1 == SelectionMethod.TOURNAMENTS_D || selectionMethod2 == SelectionMethod.TOURNAMENTS_D ||
                    selectionMethod1 == SelectionMethod.TOURNAMENTS_P || selectionMethod2 == SelectionMethod.TOURNAMENTS_P)
                tournamentCombatants = Integer.parseInt(p.getProperty("TOURNAMENT_COMBATANT_NUMBER"));
            else tournamentCombatants = 0;

            crossoverType = CrossoverType.valueOf(p.getProperty("CROSSOVER_TYPE"));
            //crossoverPair = CrossoverPair.valueOf(p.getProperty("CROSSOVER_PAIR"));
            crossoverProbability = Double.parseDouble(p.getProperty("CROSSOVER_PROBABILITY"));
            stopCriteria = StopCriteria.valueOf(p.getProperty("STOP_CRITERIA"));
            if(stopCriteria == StopCriteria.TIME) {
                time = Long.parseLong(p.getProperty("TIME"));
            }
            maxGenerations = Integer.parseInt(p.getProperty("MAX_GENERATIONS"));
            bestCharacterRepetitionLimit = Integer.parseInt(p.getProperty("BEST_CHARACTER_REPETITION_LIMIT"));
            maxRepeatedPercentageGenerations = Integer.parseInt(p.getProperty("MAX_REPEATED_GENERATIONS"));

            poolRepetitionPercentageLimit = Double.parseDouble(p.getProperty("POOL_PERCENTAGE_REPETITION_LIMIT"));
            fitnessOptimum = Double.parseDouble(p.getProperty("FITNESS_OPTIMUM"));

            replacementType = ReplacementType.valueOf(p.getProperty("REPLACEMENT_TYPE"));
            replacementMethod1 = SelectionMethod.valueOf(p.getProperty("REPLACEMENT_METHOD_1"));
            replacementMethod2 = SelectionMethod.valueOf(p.getProperty("REPLACEMENT_METHOD_2"));
            replacementMethod1Percentage = Float.parseFloat(p.getProperty("REPLACEMENT_METHOD_1_PERCENTAGE"));

            poolSize = Integer.parseInt(p.getProperty("POOL_SIZE"));

            recolectionPeriod = Integer.parseInt(p.getProperty("RECOLECTION_PERIOD"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }

    public static int getNumberOfGenes() {
        return NUMBER_OF_GENES;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public SelectionMethod getSelectionMethod1() {
        return selectionMethod1;
    }

    public SelectionMethod getSelectionMethod2() {
        return selectionMethod2;
    }

    public Float getSelectionMethod1Percentage() {
        return selectionMethod1Percentage;
    }

    public Integer getSelectionQuantity() {
        return selectionQuantity;
    }

    public Integer getTournamentCombatants() {
        return tournamentCombatants;
    }

    public StopCriteria getStopCriteria() {
        return stopCriteria;
    }

    public Integer getMaxGenerations() {
        return maxGenerations;
    }

    public Integer getBestCharacterRepetitionLimit() {
        return bestCharacterRepetitionLimit;
    }

    public Integer getMaxRepeatedPercentageGenerations() {
        return maxRepeatedPercentageGenerations;
    }

    public Double getPoolRepetitionPercentageLimit() {
        return poolRepetitionPercentageLimit;
    }

    public Double getFitnessOptimum() {
        return fitnessOptimum;
    }

    public MutationType getMutationType() {
        return mutationType;
    }

    public Double getMutationProbability() {
        return mutationProbability;
    }

    public CrossoverType getCrossoverType() {
        return crossoverType;
    }



    public Double getCrossoverProbability() {
        return crossoverProbability;
    }

    public ReplacementType getReplacementType() {
        return replacementType;
    }

    public SelectionMethod getReplacementMethod1() {
        return replacementMethod1;
    }

    public SelectionMethod getReplacementMethod2() {
        return replacementMethod2;
    }

    public Float getReplacementMethod1Percentage() {
        return replacementMethod1Percentage;
    }

    public Double getAttackCoefficient() {
        return attackCoefficient;
    }

    public Double getDefenseCoefficient() {
        return defenseCoefficient;
    }

    public Double getStrengthCoefficient() {
        return strengthCoefficient;
    }

    public Double getAgilityCoefficient() {
        return agilityCoefficient;
    }

    public Double getExpertiseCoefficient() {
        return expertiseCoefficient;
    }

    public Double getResistanceCoefficient() {
        return resistanceCoefficient;
    }

    public Double getVitalityCoefficient() {
        return vitalityCoefficient;
    }

    public int getRecolectionPeriod() {
        return recolectionPeriod;
    }


    public long getTime() {
        return time;
    }
}
