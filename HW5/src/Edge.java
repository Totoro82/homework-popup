/**
 * Represents a directed weighted edge in a graph.
 *
 * @author David Gonçalves
 * @author Elias Rilegård
 */
public class Edge {
    final int to;
    final int weight;

    /**
     * Creates a new edge.
     *
     * @param to     the destination node
     * @param weight the weight of the edge
     */
    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}
