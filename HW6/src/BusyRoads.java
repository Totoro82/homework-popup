import util.graph.core.Edge;
import util.graph.core.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * O(E * log(V))
 */
public class BusyRoads {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());//number of cities
        int m = Integer.parseInt(st.nextToken());//number of roads
        int c = Integer.parseInt(st.nextToken());//s/day

        Graph<TimeEdgeWithConstraints> graph = new Graph<>(n);

        for (int i = 0; i < m; i++) {//roads(bidirectional)
            st =  new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());

            graph.addEdge(a - 1, new TimeEdgeWithConstraints(b - 1, t, l, r));
            graph.addEdge(b - 1, new TimeEdgeWithConstraints(a - 1, t, l, r));
        }

        long[] timeToNode = new long[n];
        Arrays.fill(timeToNode, Long.MAX_VALUE);
        timeToNode[0] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, 0});//totalTime, node

        while(!pq.isEmpty()) {
            long[] top = pq.poll();
            long t = top[0];
            int u = (int) top[1];
            if (t > timeToNode[u]) continue;

            if (u == n - 1) {
                System.out.println(t);
                return;
            }

            for(TimeEdgeWithConstraints e: graph.neighbors(u)) {
                int secondOfDay = (int)(t % c); // cause of totalTime % c seconds per day recurringly each day
                long wait;

                if (secondOfDay >= e.startSecond() && secondOfDay <= e.endSecond()) {
                    wait = 0;
                } else if (secondOfDay < e.startSecond()) {
                    wait = e.startSecond() - secondOfDay;
                } else {
                    wait = (c - secondOfDay) + e.startSecond();
                }

                long arrivalTime = t + wait + e.time();

                if (arrivalTime < timeToNode[e.to()]) {
                    timeToNode[e.to()] = arrivalTime;
                    pq.add(new long[]{arrivalTime, e.to()});
                }
            }
        }
    }
}

record TimeEdgeWithConstraints(int to, int time, int startSecond, int endSecond) implements Edge{}
