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
    private Double crossoverProbability;
    private CrossoverParentSelection crossoverParentSelection;

    private ReplacementType replacementType;
    private SelectionMethod replacementMethod1;
    private SelectionMethod replacementMethod2;
    private Float replacementMethod1Percentage;

    private Double attackCoefficient;
    private Double defenseCoefficient;

    private long time;
    private boolean mutationHeat;

    private long seed;


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


            mutationProbability = Double.parseDouble(p.getProperty("MUTATION_PROBABILITY"));
            mutationType = MutationType.valueOf(p.getProperty("MUTATION_TYPE"));
            mutationHeat = Boolean.valueOf(p.getProperty("MUTATION_HEAT"));

            selectionMethod1 = SelectionMethod.valueOf(p.getProperty("SELECTION_METHOD_1"));
            selectionMethod2 = SelectionMethod.valueOf(p.getProperty("SELECTION_METHOD_2"));
            selectionMethod1Percentage = Float.parseFloat(p.getProperty("SELECTION_METHOD_1_PERCENTAGE"));
            selectionQuantity = Integer.parseInt(p.getProperty("SELECTION_QUANTITY"));

            if(selectionMethod1 == SelectionMethod.TOURNAMENTS_D || selectionMethod2 == SelectionMethod.TOURNAMENTS_D ||
                    selectionMethod1 == SelectionMethod.TOURNAMENTS_P || selectionMethod2 == SelectionMethod.TOURNAMENTS_P)
                tournamentCombatants = Integer.parseInt(p.getProperty("TOURNAMENT_COMBATANT_NUMBER"));
            else tournamentCombatants = 0;

            crossoverType = CrossoverType.valueOf(p.getProperty("CROSSOVER_TYPE"));
            crossoverProbability = Double.parseDouble(p.getProperty("CROSSOVER_PROBABILITY"));
            crossoverParentSelection = CrossoverParentSelection.valueOf(p.getProperty("CROSSOVER_COUPLE_SELECTION"));


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

            seed = Long.parseLong(p.getProperty("SEED"));

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


    public boolean isMutationHeat() {
        return mutationHeat;
    }

    public long getTime() {
        return time;
    }

    public CrossoverParentSelection getCrossoverParentSelection() {
        return crossoverParentSelection;
    }

    public long getSeed() {
        return seed;
    }

    public void setInputStream(FileInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public void setSelectionMethod1(SelectionMethod selectionMethod1) {
        this.selectionMethod1 = selectionMethod1;
    }

    public void setSelectionMethod2(SelectionMethod selectionMethod2) {
        this.selectionMethod2 = selectionMethod2;
    }

    public void setSelectionMethod1Percentage(Float selectionMethod1Percentage) {
        this.selectionMethod1Percentage = selectionMethod1Percentage;
    }

    public void setSelectionQuantity(Integer selectionQuantity) {
        this.selectionQuantity = selectionQuantity;
    }

    public void setTournamentCombatants(Integer tournamentCombatants) {
        this.tournamentCombatants = tournamentCombatants;
    }

    public void setStopCriteria(StopCriteria stopCriteria) {
        this.stopCriteria = stopCriteria;
    }

    public void setMaxGenerations(Integer maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public void setBestCharacterRepetitionLimit(Integer bestCharacterRepetitionLimit) {
        this.bestCharacterRepetitionLimit = bestCharacterRepetitionLimit;
    }

    public void setMaxRepeatedPercentageGenerations(Integer maxRepeatedPercentageGenerations) {
        this.maxRepeatedPercentageGenerations = maxRepeatedPercentageGenerations;
    }

    public void setPoolRepetitionPercentageLimit(Double poolRepetitionPercentageLimit) {
        this.poolRepetitionPercentageLimit = poolRepetitionPercentageLimit;
    }

    public void setFitnessOptimum(Double fitnessOptimum) {
        this.fitnessOptimum = fitnessOptimum;
    }

    public void setMutationType(MutationType mutationType) {
        this.mutationType = mutationType;
    }

    public void setMutationProbability(Double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public void setCrossoverType(CrossoverType crossoverType) {
        this.crossoverType = crossoverType;
    }

    public void setCrossoverProbability(Double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public void setCrossoverParentSelection(CrossoverParentSelection crossoverParentSelection) {
        this.crossoverParentSelection = crossoverParentSelection;
    }

    public void setReplacementType(ReplacementType replacementType) {
        this.replacementType = replacementType;
    }

    public void setReplacementMethod1(SelectionMethod replacementMethod1) {
        this.replacementMethod1 = replacementMethod1;
    }

    public void setReplacementMethod2(SelectionMethod replacementMethod2) {
        this.replacementMethod2 = replacementMethod2;
    }

    public void setReplacementMethod1Percentage(Float replacementMethod1Percentage) {
        this.replacementMethod1Percentage = replacementMethod1Percentage;
    }

    public void setAttackCoefficient(Double attackCoefficient) {
        this.attackCoefficient = attackCoefficient;
    }

    public void setDefenseCoefficient(Double defenseCoefficient) {
        this.defenseCoefficient = defenseCoefficient;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMutationHeat(boolean mutationHeat) {
        this.mutationHeat = mutationHeat;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }
}
