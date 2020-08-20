package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.*;
import ar.edu.itba.sia.tp1.strategies.IDGREEDY;

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
    private Node goalNode;
    private int explodedCount ;
    private Strategy strategy;
    private Optional<Heuristic> heuristic;
    private Metrics metrics;
    private boolean detect_lock;

    public Engine(Problem p, Strategy strategy, Optional<Heuristic> heuristic, boolean detect_lock) {
        this.problem = p;
        this.nodes = new PriorityQueue<Node>(strategy);
        this.goalNode = null;
        this.explodedCount = 0;
        this.metrics = new Metrics();
        this.strategy = strategy;
        this.detect_lock = detect_lock;
        if(strategy.needsHeuristic()){
            if(!heuristic.isPresent()){
                throw new IllegalArgumentException("Chosen Strategy needs Heuristic");
            }
            // if heuristic is present, then lock is detected there.
            this.detect_lock = false;
            Node.setHeuristic(heuristic);
        }else{
            Node.setHeuristic(Optional.empty());
        }
    }

    /**
     * Returns Rules applied from initial state to goal state.
     */
    public Optional<Deque<Map.Entry<State,Rule>>> solve(){
        long initialTime = System.currentTimeMillis();
        State initialState = problem.getInitialState();
        Node initialNode = new Node(initialState, 0, null, 0, null);
        nodes.add(initialNode);
        Node currentNode = null;
        do {
            while (!nodes.isEmpty() && !strategy.hasFinished()) {
                currentNode = nodes.poll();
                State currentState = currentNode.getState();

                if (!strategy.needsExploring(currentNode)) {
                    continue;
                }
                strategy.visit(currentNode);
                //System.out.println(currentNode.getCost() + "-" + currentNode.getHeuristicValue());

                if (problem.isGoal(currentState)) {
                    goalNode = currentNode;
                    strategy.finished(goalNode);
                    break;
                }
                // check lock condition
                if (detect_lock && problem.isLock(currentState, currentNode.getBirthRule())) {
                    continue;
                }
                explodedCount++;
                List<Node> des = problem.getDescendants(currentNode);

                des.forEach(n -> {
                    if (n.getHeuristicValue() < Double.MAX_VALUE && strategy.needsExploring(n)) {
                        nodes.add(n);
                    }
                });

            }

            nodes.addAll(strategy.nextIteration());

        }while(!nodes.isEmpty() && !strategy.hasFinished() );

        metrics.setElapsedTime(System.currentTimeMillis() - initialTime);

        if( goalNode == null){
            return Optional.empty();
        }
        metrics.setCost(goalNode.getCost());
        metrics.setDepth(goalNode.getDepth());
        metrics.setExpandedNodes(explodedCount);
        // TODO: no tengo en cuenta los auxiliares
        metrics.setBoundaryNodes(nodes.size());

        Deque<Map.Entry<State,Rule>> rulesApplied = new LinkedList<>();

        while(currentNode.getParent() != null){
            rulesApplied.push(Map.entry(currentNode.getState(),currentNode.getBirthRule()));
            currentNode = currentNode.getParent();
        }
        rulesApplied.push(Map.entry(currentNode.getState(), new Rule() {
            @Override
            public Optional<State> apply(State state) {
                return Optional.empty();
            }

            @Override
            public int getCost() {
                return 0;
            }

            @Override
            public String getName() {
                return "Initial State";
            }
        }));



        return Optional.of(rulesApplied);
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public Node getGoalNode() {
        return goalNode;
    }
}
