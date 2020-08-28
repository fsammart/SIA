package ar.edu.itba.sia.tp2.models;

import java.util.Objects;

public class Token {
    private Integer id;
    private Double strength;
    private Double agility;
    private Double expertise;
    private Double resistance;
    private Double vitality;

    public Token(Integer id, Double strength, Double agility, Double expertise, Double resistance, Double vitality) {
        this.id = id;
        this.strength = strength;
        this.agility = agility;
        this.expertise = expertise;
        this.resistance = resistance;
        this.vitality = vitality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    public Double getAgility() {
        return agility;
    }

    public void setAgility(Double agility) {
        this.agility = agility;
    }

    public Double getExpertise() {
        return expertise;
    }

    public void setExpertise(Double expertise) {
        this.expertise = expertise;
    }

    public Double getResistance() {
        return resistance;
    }

    public void setResistance(Double resistance) {
        this.resistance = resistance;
    }

    public Double getVitality() {
        return vitality;
    }

    public void setVitality(Double vitality) {
        this.vitality = vitality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(id, token.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id: " + id +
                ", strength: " + strength +
                ", agility: " + agility +
                ", expertise: " + expertise +
                ", resistance: " + resistance +
                ", vitality: " + vitality;
    }
}
