import util.graph.core.Graph;
import util.graph.eulerian.EulerianWalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

public class RoadPlowing {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); //number of houses
        int c = Integer.parseInt(st.nextToken()); //number of roads

        Graph<EulerianWalk.DirectedEdge> graph = new Graph<>(n);

        for (int i = 0; i < c; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken()) - 1;
            graph.addEdge(a, new EulerianWalk.DirectedEdge(a, b));
            graph.addEdge(b, new EulerianWalk.DirectedEdge(b, a));
        }

        List<EulerianWalk.DirectedEdge> walk = EulerianWalk.eulerianWalk(graph).get();

        StringBuilder sb = new StringBuilder();
        sb.append(walk.get(0).from() + 1);
        for (EulerianWalk.DirectedEdge edge : walk) {
            sb.append(' ').append(edge.to() + 1);
        }
        System.out.println(sb);
    }

}
