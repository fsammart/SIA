package ar.edu.itba.sia.tp2.models;

import java.util.Objects;

public class Warrior implements Comparable<Warrior> {

    // coefficients for current warrior.
    private static double attackCoefficient;
    private static double defenseCoefficient;

    private static Warrior bestWarrior = null;

    private static int geneNumber = 6;


    private Token weapon;
    private Token boots;
    private Token helmet;
    private Token gloves;
    private Token breastPlate;
    private double height;

    private double fitness;

    public static void setCoefficients(double attackCoefficient, double defenseCoefficient){
        Warrior.attackCoefficient = attackCoefficient;
        Warrior.defenseCoefficient = defenseCoefficient;

    }

    public Warrior(Token weapon, Token boots, Token helmet, Token gloves, Token breastPlate, double height){
        this.weapon = weapon;
        this.boots = boots;
        this.helmet = helmet;
        this.gloves = gloves;
        this.breastPlate = breastPlate;
        this.height = height;
        getFitness();

    }

    public Double getFitness() {
        Double fitness =  attackCoefficient * getAttack() + defenseCoefficient * getDefense();
        this.fitness = fitness;

        return fitness;
    }

    private Double getAttack() {
        return (getAgility() + getExpertise()) * getStrength() * getAttackHeightModifier();
    }

    private Double getDefense() {
        return (getResistance() + getExpertise()) * getVitality() * getDefenseHeightModifier();
    }

    public Double getAgility() {
        Double itemAgility = helmet.getAgility() + weapon.getAgility() + boots.getAgility() +
                gloves.getAgility() + breastPlate.getAgility();

        return Math.tanh(0.01 * itemAgility );
    }

    public Double getResistance() {
        Double itemResistance = helmet.getResistance() + weapon.getResistance() + boots.getResistance() +
                gloves.getResistance() + breastPlate.getResistance();

        return Math.tanh(0.01 * itemResistance );
    }

    public Double getExpertise() {
        Double itemExpertise = helmet.getExpertise() + weapon.getExpertise() + boots.getExpertise() +
                gloves.getExpertise() + breastPlate.getExpertise();

        return 0.6 * Math.tanh(0.01 * itemExpertise );
    }

    public Double getStrength() {
        Double itemStrength = helmet.getStrength() + weapon.getStrength() + boots.getStrength() +
                gloves.getStrength() + breastPlate.getStrength();

        return 100 * Math.tanh(0.01 * itemStrength  );
    }

    public Double getVitality() {
        Double itemVitality = helmet.getVitality() + weapon.getVitality() + boots.getVitality() +
                gloves.getVitality() + breastPlate.getVitality();

        return 100 * Math.tanh(0.01 * itemVitality );
    }

    public Double getAttackHeightModifier() {
        return 0.7 - Math.pow((3 * height - 5) , 4) + Math.pow((3 * height - 5) , 2) + height/4;
    }

    public Double getDefenseHeightModifier() {
        return 1.9 + Math.pow((2.5 * height - 4.16) , 4) - Math.pow((2.5 * height - 4.16) , 2) - 3*height/10;
    }

    @Override
    public int compareTo(Warrior o) {
        return Double.compare(getFitness(), o.getFitness());
    }


    public static void setAttackCoefficient(double attackCoefficient) {
        Warrior.attackCoefficient = attackCoefficient;
    }


    public void setHeight(double height) {
        this.height = height;
        getFitness();
    }


    @Override
    public Warrior clone() {
        Warrior clone = new Warrior(weapon,boots,helmet,gloves,breastPlate,height);
        return clone;
    }

    public static int getGeneNumber() {
        return geneNumber;
    }

    public void setToken(Gene type, Token t){
        switch(type){
            case WEAPON: this.weapon = t; break;
            case BOOTS: this.boots = t; break;
            case HELMET: this.helmet = t; break;
            case GLOVES: this.gloves = t; break;
            case BREASTPLATE: this.breastPlate = t; break;
        }
        getFitness();
    }

    public Token getToken(Gene type){
        switch(type){
            case WEAPON: return weapon;
            case BOOTS: return boots;
            case HELMET: return helmet;
            case GLOVES: return gloves;
            case BREASTPLATE: return breastPlate;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Weapon: " + weapon +
                "\nBoots: " + boots +
                "\nHelmet: " + helmet +
                "\nGloves: " + gloves+
                "\nBreastPlate: " + breastPlate +
                "\nHeight: " + String.format("%.4f",height) +
                "\nFitness: " + String.format("%.4f",fitness);
    }

    public static Warrior getBestWarrior() {
        return bestWarrior;
    }

    public boolean isApproximateEqualTo(Warrior o){
        if (this == o) return true;
        if(o == null) return false;

        return Math.abs(o.height - height) <  0.0001 &&
                Double.compare(o.fitness, this.fitness) == 0 &&
                Objects.equals(weapon, o.weapon) &&
                Objects.equals(boots, o.boots) &&
                Objects.equals(helmet, o.helmet) &&
                Objects.equals(gloves, o.gloves) &&
                Objects.equals(breastPlate, o.breastPlate);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warrior warrior = (Warrior) o;
        return  Double.compare(warrior.height,height) == 0 &&
                Double.compare(warrior.fitness, this.fitness) == 0 &&
                Objects.equals(weapon, warrior.weapon) &&
                Objects.equals(boots, warrior.boots) &&
                Objects.equals(helmet, warrior.helmet) &&
                Objects.equals(gloves, warrior.gloves) &&
                Objects.equals(breastPlate, warrior.breastPlate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(weapon, boots, helmet, gloves, breastPlate, height, fitness);
    }

    public static void setBestWarrior(Warrior bestWarrior) {
        Warrior.bestWarrior = bestWarrior;
    }

    public double getHeight() {
        return height;
    }
}
