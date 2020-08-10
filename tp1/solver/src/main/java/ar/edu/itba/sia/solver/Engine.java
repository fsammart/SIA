package ar.edu.itba.sia.solver;

import ar.edu.itba.sia.solver.api.Problem;
import ar.edu.itba.sia.solver.api.Rule;
import ar.edu.itba.sia.solver.api.State;

import java.awt.*;
import java.util.*;

public class Engine {

    /**
     * Using deque for both dfs and bfs strategies
     *
     * In dfs usign offerFirst (stack) and bfs just offer (queue)
      */

    private Deque<Node> nodes;
    private Problem problem;
    private Map<State,Integer> visited; /* store visited with their cost (depth) */
    private boolean finished;
    private Node goalNode;

    public Engine(Problem p) {
        this.problem = problem;
        this.nodes = new LinkedList<Node>();
        this.finished = false;
        this.goalNode = null;
    }

    /**
     * Returns Rules applied from initial state to goal state.
     */
    public Optional<Deque<Rule>> solve(){
        State initialState = problem.getInitialState();
        Node initialNode = new Node(initialState, 0, null, 0, null);
        nodes.push(initialNode);
        Node currentNode = null;
        while(!nodes.isEmpty() && !finished){
            currentNode = nodes.pop();
            State currentState = currentNode.getState();
            int currentDepth = currentNode.getDepth();
            int currentCost = currentNode.getCost();

            if(visited.containsKey(currentState)
                    && visited.get(currentState) <= currentCost){
                //already visited with lower cost
                continue;
            }

            visited.put(currentState, currentCost);

            if(problem.isGoal(currentState)){
                finished = true;
                goalNode = currentNode;
                break;
            }

            Map<State, Map.Entry<Rule, Integer>> des = problem.getDescendants(currentNode.getState());

            // For effectively final
            Node aux = currentNode;
            des.forEach((State s, Map.Entry<Rule,Integer> m) ->{
                if(!visited.containsKey(s)){
                    nodes.push(new Node(s, currentDepth + 1, aux, m.getValue(),m.getKey()));
                }
            });

        }

        if(!finished){
            return Optional.empty();
        }

        Deque<Rule> rulesApplied = new LinkedList<>();

        while(currentNode.getParent() != null){
            rulesApplied.push(currentNode.getBirthRule());
            currentNode = currentNode.getParent();
        }

        return Optional.of(rulesApplied);
    }

}
