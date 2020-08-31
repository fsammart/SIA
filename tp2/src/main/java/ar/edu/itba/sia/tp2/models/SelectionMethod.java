package ar.edu.itba.sia.tp2.models;

import ar.edu.itba.sia.tp2.utils.SRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public enum SelectionMethod {
    RANKING {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return ranking(l, size);
        }
    }, ELITE {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return elite(l, size);
        }
    }, ROULETTE {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return roulette(l, size);
        }
    }, TOURNAMENTS_D {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return tournamentProbabilistic(l, size, combat_size, 1);
        }
    }, TOURNAMENTS_P {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return tournamentProbabilistic(l, size, combat_size,0.75);
        }
    }, BOLTZMANN_ROULETTE {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return boltzmannRoulette(l, size, generation);
        }
    }, UNIVERSAL {
        @Override
        public List<Warrior> select(List<Warrior> l, int size,int combat_size, int generation) {
            return universal(l, size);
        }
    };


    public abstract List<Warrior> select(List<Warrior> l, int size, int combat_size, int generation);

    private static List<Warrior> boltzmannRoulette(List<Warrior> Warriors, int k, int generations) {
        double temperature = 100.0 / (generations + 1) + 0.001;
        double avgValue = averageBoltzmannExpression(Warriors, temperature);
        double randomNumbers[] = SRandom.r.doubles(k).toArray();
        Double fitness[] = Warriors.stream().map(c -> Math.exp(c.getFitness() / temperature) / avgValue)
                .toArray(Double[]::new);

        return randomCumulativeSelection(Warriors, randomNumbers, fitness);
    }

    private static double averageBoltzmannExpression(List<Warrior> Warriors, double temperature) {
        return Warriors.stream()
                .map(c -> Math.exp(c.getFitness() / temperature))
                .reduce(0.0, Double::sum) / Warriors.size();

    }

    private static List<Warrior> universal(List<Warrior> Warriors, int k) {
        double firstRandom = SRandom.r.nextDouble();
        double randomNumbers[] = new double[k];
        Double fitness[] = getFitness(Warriors);

        for (int i = 0; i < k; i++) {
            randomNumbers[i] = (firstRandom + i) / k;
        }

        return randomCumulativeSelection(Warriors, randomNumbers, fitness);
    }

    private static List<Warrior> tournamentProbabilistic(List<Warrior> Warriors, int k,
                                                           int m, double probability) {
        final List<Warrior> selection = new ArrayList<>();
        final List<Warrior> challengers = new ArrayList<>();

        IntStream.range(0, k).forEach(x -> {
            Warrior winner = null;
            challengers.clear();
            boolean maxFitnessWinner = SRandom.r.nextDouble() < probability;

            for (int j = 0; j < m; j++) {
                int index = SRandom.r.nextInt(Warriors.size());
                challengers.add(Warriors.get(index));
            }
            if (maxFitnessWinner) {
                winner = challengers.stream().max(Comparator.naturalOrder()).get();
            } else {
                winner = challengers.stream().min(Comparator.naturalOrder()).get();
            }
            selection.add(winner);
        });

        return selection;
    }

    private static Double[] getFitness(List<Warrior> Warriors) {
        return Warriors.stream().map(c -> c.getFitness()).toArray(Double[]::new);

    }

    private static List<Warrior> roulette(List<Warrior> Warriors, int k) {
        double randomNumbers[] = SRandom.r.doubles(k).toArray();
        Double fitness[] = getFitness(Warriors);
        return randomCumulativeSelection(Warriors, randomNumbers, fitness);
    }

    private static List<Warrior> ranking(List<Warrior> l, int k) {
        Collections.sort(l);
        double randomNumbers[] = new double[k];
        Double fitness[] = new Double[l.size()];

        for (int i = 0; i < k; i++) {
            randomNumbers[i] = SRandom.r.nextDouble();
        }

        for (int i = 0; i < l.size(); i++) {
            fitness[i] = (i + 1.0);
        }

        return randomCumulativeSelection(l, randomNumbers, fitness);
    }

    private static List<Warrior> elite(List<Warrior> Warriors, int k) {
        Collections.sort(Warriors, Comparator.reverseOrder()); // Se ordena al reves (de mayor a menor)
        List<Warrior> nlist = new ArrayList<>();
        nlist.addAll(Warriors.subList(0, k)); // Los k mejores
        return nlist;

    }

    private static List<Warrior> randomCumulativeSelection(List<Warrior> Warriors, double randomNumbers[],
                                                             Double[] fitness) {
        List<Warrior> selection = new ArrayList<>();
        boolean firstFound = false;
        Double cumulativeFitness[] = new Double[Warriors.size()];
        Double fitnessSum;
        cumulativeFitness[0] = fitness[0];

        for (int j = 1; j < Warriors.size(); j++) {
            cumulativeFitness[j] = cumulativeFitness[j - 1] + fitness[j];
        }

        fitnessSum = cumulativeFitness[cumulativeFitness.length - 1];

        for (int i = 0; i < randomNumbers.length; i++) {
            double randomNumber = randomNumbers[i];

            for (int j = 0; j < cumulativeFitness.length && !firstFound; j++) {
                double r = cumulativeFitness[j] / fitnessSum;
                if (r > randomNumber) {
                    selection.add(Warriors.get(j));
                    firstFound = true;
                }
            }
            firstFound = false;
        }
        return selection;
    }
}
