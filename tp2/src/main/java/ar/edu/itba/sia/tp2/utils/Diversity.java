package ar.edu.itba.sia.tp2.utils;

import ar.edu.itba.sia.tp2.models.Gene;
import ar.edu.itba.sia.tp2.models.Token;
import ar.edu.itba.sia.tp2.models.Warrior;

import java.util.*;
import java.util.stream.Collectors;

public class Diversity {

    public static  double getDiversityPercentage(List<Warrior> population){
        List<Warrior> seen = new ArrayList<>();
        
        population.stream().forEach(p->{
            if(!seen.stream().anyMatch(s -> s.isApproximateEqualTo(p))){
                seen.add(p);
            }
        });
        
        return ((double)seen.size())/population.size();
    }
    
    public static Map<Gene,Double> getGeneDiversityPercentage(List<Warrior> population){
        Map<Gene,Double> result= new HashMap<>();
        List<Warrior> lastPopulation = population;
        List<Token> Tokens;
        Set<Token> setTokens = new HashSet<>();
        //Weapons
        Tokens = lastPopulation.stream().map(p -> p.getToken(Gene.WEAPON)).collect(Collectors.toList());
        setTokens.addAll(Tokens);

        result.put(Gene.WEAPON,((double)setTokens.size())/Tokens.size());

        //Boots
        Tokens.clear();
        setTokens.clear();
        Tokens = lastPopulation.stream().map(p -> p.getToken(Gene.BOOTS)).collect(Collectors.toList());
        setTokens.addAll(Tokens);

        result.put(Gene.BOOTS,((double)setTokens.size())/Tokens.size());

        //Helmets
        Tokens.clear();
        setTokens.clear();
        Tokens = lastPopulation.stream().map(p -> p.getToken(Gene.HELMET)).collect(Collectors.toList());
        setTokens.addAll(Tokens);

        result.put(Gene.HELMET,((double)setTokens.size())/Tokens.size());

        //Gloves
        Tokens.clear();
        setTokens.clear();
        Tokens = lastPopulation.stream().map(p -> p.getToken(Gene.GLOVES)).collect(Collectors.toList());
        setTokens.addAll(Tokens);

        result.put(Gene.GLOVES,((double)setTokens.size())/Tokens.size());

        //BREASTPLATE
        Tokens.clear();
        setTokens.clear();
        Tokens = lastPopulation.stream().map(p -> p.getToken(Gene.BREASTPLATE)).collect(Collectors.toList());
        setTokens.addAll(Tokens);

        result.put(Gene.BREASTPLATE,((double)setTokens.size())/Tokens.size());

        //HEIGHT
        List<Double> heights = lastPopulation.stream().map(Warrior::getHeight).map(d -> Round.round(d,4)).collect(Collectors.toList());
        Set<Double> setHeight = new HashSet<>(heights);

        result.put(Gene.HEIGHT,((double)setTokens.size())/Tokens.size());

        return result;
    }
}
