package ar.edu.itba.sia.tp1;

import ar.edu.itba.sia.tp1.api.Problem;
import ar.edu.itba.sia.tp1.api.Rule;
import ar.edu.itba.sia.tp1.api.State;

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
    private int explodedCount ;

    public Engine(Problem p) {
        this.problem = p;
        this.nodes = new LinkedList<Node>();
        this.finished = false;
        this.goalNode = null;
        this.visited = new HashMap<>();
        this.explodedCount = 0;
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

            // check lock condition

            if(problem.isLock(currentState)){
                continue;
            }
            explodedCount ++;
            Map<Rule, State> des = problem.getDescendants(currentNode.getState());

            // For effectively final
            Node aux = currentNode;
            int acumCost = currentNode.getCost();
            des.forEach((Rule r, State s) ->{
                if(!(visited.containsKey(s) && visited.get(s) <= acumCost + r.getCost())){
                    nodes.push(new Node(s, currentDepth + 1,
                            aux, acumCost+ r.getCost(),r));
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
