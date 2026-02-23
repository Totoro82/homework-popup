import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class Prim {
    public record MinimumSpanningTree(int totalWeight, List<UndirectedEdge> edges) {}
    // This would be redundant if we had a from field on Edge
    public record UndirectedEdge(int from, int to, int weight) {}

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in);

        while (io.hasMoreTokens()) {
            int n = io.getInt();
            int m = io.getInt();

            if (n == 0) break;

            Graph graph = new Graph(n);
            for (int i = 0; i < m; i++) {
                int u = io.getInt();
                int v = io.getInt();
                int w = io.getInt();
                graph.addEdge(u, v, w);
                graph.addEdge(v, u, w); // Undirected graph: add edge in both directions
            }

            // Micro-optimization: Tree only if |E| >= |V| - 1
            // if (m < n - 1) {
            //     System.out.println("Impossible");
            //     continue;
            // }

            Optional<MinimumSpanningTree> result = prim(graph);
            if (result.isEmpty()) {
                System.out.println("Impossible");
            } else {
                MinimumSpanningTree mst = result.get();

                int totalWeight = mst.totalWeight();
                System.out.println(totalWeight);

                // Edge output should be x < y, sorted by x then y
                List<UndirectedEdge> edges = mst.edges();
                edges.sort(Comparator.comparingInt((UndirectedEdge e) -> Math.min(e.from, e.to))
                        .thenComparingInt(e -> Math.max(e.from, e.to)));
                for (UndirectedEdge edge : edges) {
                    int x = Math.min(edge.from, edge.to);
                    int y = Math.max(edge.from, edge.to);
                    System.out.println(x + " " + y);
                }
            }
        }
    }

    public static Optional<MinimumSpanningTree> prim(Graph graph) {
        int n = graph.nodeCount();
        if (n == 0) return Optional.empty();

        PriorityQueue<UndirectedEdge> pq = new PriorityQueue<>(Comparator.comparingInt((UndirectedEdge e) -> e.weight));
        boolean[] visited = new boolean[n];

        List<UndirectedEdge> mstEdges = new ArrayList<>();
        int totalWeight = 0;

        // Start from node 0 (arbitrary choice)
        visited[0] = true;
        for (Edge edge : graph.neighbors(0)) {
            pq.add(new UndirectedEdge(0, edge.to, edge.weight));
        }

        while (!pq.isEmpty()) {
            UndirectedEdge current = pq.poll();
            int node = current.to;
            int weight = current.weight;

            if (visited[node]) continue;

            visited[node] = true;
            totalWeight += weight;
            mstEdges.add(current);

            for (Edge edge : graph.neighbors(node)) {
                if (!visited[edge.to]) {
                    pq.add(new UndirectedEdge(node, edge.to, edge.weight));
                }
            }
        }

        if (mstEdges.size() != n - 1) return Optional.empty(); // Not all nodes were visited, graph is not fully connected

        return Optional.of(new MinimumSpanningTree(totalWeight, mstEdges));
    }
}
