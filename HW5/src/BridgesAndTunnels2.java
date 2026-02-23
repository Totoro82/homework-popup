import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BridgesAndTunnels2 {
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in);

        int n = io.getInt();
        int m = io.getInt();
        int p = io.getInt();

        Graph graph = new Graph(n);
        for (int i = 0; i < m; i++) {
            int u = io.getInt();
            int v = io.getInt();
            int w = io.getInt();
            boolean outdoor = io.getWord().equals("O");
            // Undirected graph
            graph.addEdge(u, new OutdoorEdge(v, w, outdoor));
            graph.addEdge(v, new OutdoorEdge(u, w, outdoor));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p; i++) {
            int from = io.getInt();
            int to = io.getInt();
            long[] result = dijkstra(graph, from, to);
            if (result == null) {
                sb.append("IMPOSSIBLE");
            } else {
                sb.append(result[0]).append(' ').append(result[1]);
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }

    // Returns {outdoorTime, totalTime} or null if unreachable
    // Minimizes outdoor first, then total (lexicographic)
    public static long[] dijkstra(Graph graph, int source, int target) {
        int n = graph.nodeCount();
        // dist[i] = {outdoor, total}
        long[][] dist = new long[n][2];
        for (long[] d : dist) Arrays.fill(d, Long.MAX_VALUE);
        dist[source][0] = 0;
        dist[source][1] = 0;

        // PQ entries: {outdoor, total, node}
        PriorityQueue<long[]> pq = new PriorityQueue<>(
            Comparator.comparingLong((long[] a) -> a[0]).thenComparingLong(a -> a[1])
        );
        pq.add(new long[]{0, 0, source});

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            long outd = top[0], total = top[1];
            int u = (int) top[2];

            // Stale entry
            if (outd > dist[u][0] || (outd == dist[u][0] && total > dist[u][1])) continue;

            if (u == target) return new long[]{outd, total}; // Early exit

            for (Edge e : graph.neighbors(u)) {
                OutdoorEdge oe = (OutdoorEdge) e;
                long newOutd = outd + oe.outdoorWeight;
                long newTotal = total + oe.weight;

                // Lexicographic: outdoor first, then total
                if (newOutd < dist[e.to][0] ||
                    (newOutd == dist[e.to][0] && newTotal < dist[e.to][1])) {
                    dist[e.to][0] = newOutd;
                    dist[e.to][1] = newTotal;
                    pq.add(new long[]{newOutd, newTotal, e.to});
                }
            }
        }

        return dist[target][0] == Long.MAX_VALUE ? null : dist[target];
    }
}

