package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.*;

import java.util.*;

public class Engine {

    /**
     * Using deque for both dfs and bfs strategies
     *
     * In dfs usign offerFirst (stack) and bfs just offer (queue)
      */

    private PriorityQueue<Node> nodes;
    private Problem problem;
    private Map<State,Integer> visited; /* store visited with their cost (depth) */
    private boolean finished;
    private Node goalNode;
    private int explodedCount ;
    private Strategy strategy;
    private Optional<Heuristic> heuristic;

    public Engine(Problem p, Strategy strategy, Optional<Heuristic> heuristic) {
        this.problem = p;
        this.nodes = new PriorityQueue<Node>(strategy);
        this.finished = false;
        this.goalNode = null;
        this.explodedCount = 0;
        this.strategy = strategy;
        if(strategy.needsHeuristic()){
            if(!heuristic.isPresent()){
                throw new IllegalArgumentException("Chosen Strrategy needs Heuristic");
            }
            Node.setHeuristic(heuristic);
        }else{
            Node.setHeuristic(Optional.empty());
        }
    }

    /**
     * Returns Rules applied from initial state to goal state.
     */
    public Optional<Deque<Rule>> solve(){
        State initialState = problem.getInitialState();
        Node initialNode = new Node(initialState, 0, null, 0, null);
        nodes.add(initialNode);
        Node currentNode = null;
        while(!nodes.isEmpty() && !finished){
            currentNode = nodes.poll();

            State currentState = currentNode.getState();
            int currentDepth = currentNode.getDepth();

            //System.out.println(currentState);

            if(!strategy.needsExploring(currentNode)){
                continue;
            }

            strategy.visit(currentNode);

            if(problem.isGoal(currentState)){
                finished = true;
                goalNode = currentNode;
                break;
            }

            // check lock condition

            if(problem.isLock(currentState, currentNode.getBirthRule())){
                // visited.put(currentState,0);
                continue;
            }
            explodedCount ++;
            List<Node> des = problem.getDescendants(currentNode);

            // For effectively final
            Node aux = currentNode;
            int acumCost = currentNode.getCost();
            des.forEach(n ->{
                if(strategy.needsExploring(n)){
                    nodes.add(n);
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
