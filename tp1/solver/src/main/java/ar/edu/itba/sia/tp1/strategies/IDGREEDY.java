package ar.edu.itba.sia.tp1.strategies;

import ar.edu.itba.sia.tp1.Node;
import ar.edu.itba.sia.tp1.api.State;
import ar.edu.itba.sia.tp1.api.Strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IDGREEDY extends RandomStrategy implements Strategy {

    // visited with depth
    HashMap<State, Integer> visited = new HashMap<>();

    private int step;
    private int currentMaxCost;
    Node goal;
    private boolean finished = false;
    private int minCost;
    private long initialTime = -1;
    private long timeout;

    public IDGREEDY(int step, int minCost, long timeout){
        this.step = step;
        this.currentMaxCost = -1;
        this.minCost = minCost;
        this.timeout = timeout;
    }

    @Override
    public boolean needsExploring(Node n) {
        if(this.initialTime == -1){
            this.initialTime = System.currentTimeMillis();
        }
        if(visited.containsKey(n.getState()) && visited.get(n.getState()) <= n.getCost()){
            return false;
        }

        if(currentMaxCost != -1 && (n.getCost() + n.getHeuristicValue() > currentMaxCost)){
            return false;
        }

        return true;
    }

    @Override
    public void visit(Node n) {
        visited.put(n.getState(),n.getCost());
    }

    @Override
    public int compare(Node o1, Node o2) {
        int compare =  Double.compare(o1.getHeuristicValue() ,o2.getHeuristicValue());
        return compare==0 ? getRandomPriority() : compare;
    }

    @Override
    public boolean needsHeuristic() {
        return true;
    }

    @Override
    public List<Node> nextIteration() {
        return new LinkedList<>();
    }

    @Override
    public boolean hasFinished() {
        if(this.initialTime == -1){
            this.initialTime = System.currentTimeMillis();
            return false;
        }
        if(System.currentTimeMillis() - initialTime >= timeout){
            return true;
        }
        return finished;
    }

    @Override
    public void finished(Node goalNode) {
        goal = goalNode;
        int currentGoalCost = goalNode.getCost();
        if(goal.getCost() <= minCost){
            finished =  true;
        }
        if(currentGoalCost - step >0){
            currentMaxCost = currentGoalCost -step;
        } else {
            finished = true;
        }

    }

    @Override
    public String toString() {
        return "IDGREEDY";
    }

    public int getVisited(){
        return visited.size();
    }
}
