package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.*;

public enum CrossoverParentSelection {
    RANDOM{
        @Override
        public List<Map.Entry<Warrior,Warrior>> getCouples(List<Warrior> parents) {
            Collections.shuffle(parents);
            List<Map.Entry<Warrior,Warrior>> couples = new ArrayList<>(parents.size());

            for(int i = 0; i < parents.size()-1; i+=2){
                couples.add(Map.entry(parents.get(i), parents.get(i+1)));
            }

            return couples;
        }
    },ELITE{
        @Override
        public List<Map.Entry<Warrior,Warrior>> getCouples(List<Warrior> parents) {
            parents.sort(Comparator.naturalOrder());
            List<Map.Entry<Warrior,Warrior>> couples = new ArrayList<>(parents.size());

            for(int i = 0; i < parents.size()-1; i+=2){
                couples.add(Map.entry(parents.get(i), parents.get(i+1)));
            }

            return couples;
        }
    },DIVERSITY{
        @Override
        public List<Map.Entry<Warrior,Warrior>> getCouples(List<Warrior> parents) {
            parents.sort(Comparator.naturalOrder());
            List<Map.Entry<Warrior,Warrior>> couples = new ArrayList<>(parents.size());
            int size = parents.size();
            int medium = (int) Math.floor(parents.size()/2);
            for(int i = 0; i < medium ; i+=2){
                couples.add(Map.entry(parents.get(i), parents.get(size - 1- i)));
            }

            return couples;
        }
    };

    public abstract List<Map.Entry<Warrior,Warrior>> getCouples(List<Warrior> parents);
}
