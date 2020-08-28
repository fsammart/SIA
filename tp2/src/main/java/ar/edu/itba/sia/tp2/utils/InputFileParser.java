package ar.edu.itba.sia.tp2.utils;

import ar.edu.itba.sia.tp2.models.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class InputFileParser {

    private Map<Integer, Token> weapons;
    private Map<Integer, Token> boots;
    private Map<Integer, Token> heltmets;
    private Map<Integer, Token> gauntlets;
    private Map<Integer, Token> chestplates;


    public InputFileParser() {
        weapons = parseTokens("fulldata/armas.tsv");
        boots = parseTokens("fulldata/botas.tsv");
        heltmets = parseTokens("fulldata/cascos.tsv");
        gauntlets = parseTokens("fulldata/guantes.tsv");
        chestplates = parseTokens("fulldata/pecheras.tsv");

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

    public Token getRandomToken(Genes type) {

        Integer randomId;
        Token randomToken;
        switch (type) {
            case WEAPON:
                randomId = SRandom.r.nextInt(weapons.size());
                randomToken = weapons.get(randomId);
                break;
            case BOOTS:
                randomId = SRandom.r.nextInt(boots.size());
                randomToken = boots.get(randomId);
                break;
            case HELMET:
                randomId = SRandom.r.nextInt(heltmets.size());
                randomToken = heltmets.get(randomId);
                break;
            case GAUNTLETS:
                randomId = SRandom.r.nextInt(gauntlets.size());
                randomToken = gauntlets.get(randomId);
                break;
            case CHESTPLATE:
                randomId = SRandom.r.nextInt(chestplates.size());
                randomToken = chestplates.get(randomId);
                break;
            default:
                randomToken = null;
        }
        return randomToken;
    }

}
