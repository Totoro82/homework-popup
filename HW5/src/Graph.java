import java.util.ArrayList;
import java.util.List;

/**
 * Adjacency list representation of a directed weighted graph.
 * Nodes are numbered from 0 to n-1.
 *
 * @author David Gonçalves
 * @author Elias Rilegård
 */
public class Graph {
    private final List<List<Edge>> adjacencies;

    /**
     * Creates a graph with n nodes and no edges.
     *
     * @param n the number of nodes
     */
    public Graph(int n) {
        this.adjacencies = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.adjacencies.add(new ArrayList<>());
        }
    }

    /**
     * Adds a directed edge from node u to node v with the given weight.
     *
     * @param u the source node
     * @param v the destination node
     * @param w the edge weight
     */
    public void addEdge(int u, int v, int w) {
        this.adjacencies.get(u).add(new Edge(v, w));
    }

    public void addEdge(int u, Edge edge) {
        this.adjacencies.get(u).add(edge);
    }

    /**
     * Returns the list of outgoing edges from the given node.
     *
     * @param u the node
     * @return the list of edges from u
     */
    public List<Edge> neighbors(int u) {
        return this.adjacencies.get(u);
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return the node count
     */
    public int nodeCount() {
        return this.adjacencies.size();
    }
}
