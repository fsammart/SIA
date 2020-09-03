package ar.edu.itba.sia.tp2.utils;

import ar.edu.itba.sia.tp2.models.Gene;
import ar.edu.itba.sia.tp2.models.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class InputFileParser {

    private Map<Integer, Token> gloves;
    private Map<Integer, Token> breastplates;
    private Map<Integer, Token> weapons;
    private Map<Integer, Token> boots;
    private Map<Integer, Token> helmets;


    public InputFileParser(String path) {
        boots = parseTokens(path + "/botas.tsv");
        breastplates = parseTokens(path + "/pecheras.tsv");
        weapons = parseTokens(path + "/armas.tsv");
        helmets = parseTokens(path + "/cascos.tsv");
        gloves = parseTokens(path + "/guantes.tsv");

    }

    public static Map<Integer, Token> parseTokens(String filename) {

        Map<Integer, Token> Tokens = null;

        try {

            List<String> lines = Files.readAllLines(Paths.get(filename));
            Tokens = lines.parallelStream().skip(1).map(x -> x.split("\t")).map(vals -> {
                Integer id = Integer.parseInt(vals[0]);
                Double strength = Double.parseDouble(vals[1]);
                Double agility = Double.parseDouble(vals[2]);
                Double expertise = Double.parseDouble(vals[3]);
                Double resistance = Double.parseDouble(vals[4]);
                Double vitality = Double.parseDouble(vals[5]);
                return new Token(id, strength, agility, expertise, resistance, vitality);
            }).collect(Collectors.toMap(Token::getId, x -> x));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Tokens;
    }

    public Map<Integer, Token> getGloves() {
        return gloves;
    }

    public Map<Integer, Token> getBreastplates() {
        return breastplates;
    }

    public Map<Integer, Token> getWeapons() {
        return weapons;
    }

    public Map<Integer, Token> getBoots() {
        return boots;
    }

    public Map<Integer, Token> getHelmets() {
        return helmets;
    }



}
