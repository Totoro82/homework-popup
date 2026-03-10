package util.graph.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Adjacency list representation of a directed weighted graph.
 * Nodes are numbered from 0 to n-1.
 *
 * @author David Gonçalves
 * @author Elias Rilegård
 */
public class Graph<E extends Edge> {
    private final List<List<E>> adj;

    /**
     * Creates a graph with n nodes and no edges.
     *
     * @param n the number of nodes
     */
    public Graph(int n) {
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.adj.add(new ArrayList<>());
        }
    }

    /**
     * Adds a directed edge from node u to node v with the given weight.
     *
     * @param edge the edge to add
     */
    public void addEdge(int u, E edge) {
        this.adj.get(u).add(edge);
    }

    /**
     * Returns the list of outgoing edges from the given node.
     *
     * @param u the node
     * @return the list of edges from u
     */
    public List<E> neighbors(int u) {
        return this.adj.get(u);
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return the node count
     */
    public int nodeCount() {
        return this.adj.size();
    }
}
