package ar.edu.itba.sia.tp1;

public class Metrics {
    private int expandedNodes;
    private int depth;
    private int cost;
    private int boundaryNodes;
    private long elapsedTime;

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getExpandedNodes() {
        return expandedNodes;
    }

    public int getDepth() {
        return depth;
    }

    public int getCost() {
        return cost;
    }

    public int getBoundaryNodes() {
        return boundaryNodes;
    }

    public void setExpandedNodes(int expandedNodes) {
        this.expandedNodes = expandedNodes;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setBoundaryNodes(int boundaryNodes) {
        this.boundaryNodes = boundaryNodes;
    }
}
