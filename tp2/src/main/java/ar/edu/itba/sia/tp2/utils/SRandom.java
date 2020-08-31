package ar.edu.itba.sia.tp2.utils;

import ar.edu.itba.sia.tp2.models.Gene;
import ar.edu.itba.sia.tp2.models.Token;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.Random;

public class SRandom {

    public static Random r = new Random(System.currentTimeMillis());

    public static InputFileParser ifp;

    public static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + (max - min) * r.nextDouble();
    }

    public static Token getRandomToken(Gene type){
        Token randomItem;
        switch (type) {
            case WEAPON:
                randomItem = ifp.getWeapons().get(SRandom.r.nextInt(ifp.getWeapons().size()));
                break;
            case BOOTS:
                randomItem = ifp.getBoots().get(SRandom.r.nextInt(ifp.getBoots().size()));
                break;
            case GLOVES:
                randomItem = ifp.getGloves().get(SRandom.r.nextInt(ifp.getGloves().size()));
                break;
            case BREASTPLATE:
                randomItem = ifp.getBreastplates().get(SRandom.r.nextInt(ifp.getBreastplates().size()));
                break;
            case HELMET:
                randomItem = ifp.getHelmets().get(SRandom.r.nextInt(ifp.getHelmets().size()));
                break;
            default:
                randomItem = null;
        }
        return randomItem;
    }

    public static Warrior getRandomWarrior() {
        Token weapon = getRandomToken(Gene.WEAPON);
        Token boots = getRandomToken(Gene.BOOTS);
        Token gloves = getRandomToken(Gene.GLOVES);
        Token breastPlate = getRandomToken(Gene.BREASTPLATE);
        Token helmet = getRandomToken(Gene.HELMET);

        Double height = randomDouble(1.3,2);

        Warrior w = new Warrior(weapon,boots,helmet,gloves,breastPlate,height);

        return w;
    }




}
