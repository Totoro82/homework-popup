package util.graph.core;

/**
 * Base interface for all edge types in a graph.
 * Every edge must at least specify a destination node.
 *
 * @author David Gonçalves
 * @author Elias Rilegård
 */
public interface Edge {
    /**
     * Returns the destination node of this edge.
     *
     * @return the destination node index
     */
    int to();
}
