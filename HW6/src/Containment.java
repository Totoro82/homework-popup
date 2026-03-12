import util.graph.core.CapacityEdge;
import util.graph.core.Graph;
import util.graph.flow.MaximumFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Containment {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        Set<Integer[]> defectiveCells = new HashSet<>();
        Graph<CapacityEdge> graph = new Graph<>(1002);//1000 source(exterior) and 1001 sink(contamination)


        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            defectiveCells.add(new Integer[]{x,y,z});
        }

        //right, left, up, down, front, back
        int[] dx = {1, -1, 0, 0, 0, 0};
        int[] dy = {0, 0, 1, -1, 0, 0};
        int[] dz = {0, 0, 0, 0, 1, -1};

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 10; z++) {//for every cell
                    for (int i = 0; i < 6; i++) { //for every neighbour
                        int from = x * 100 + y * 10 + z;
                        int xVecino = x + dx[i];
                        int yVecino = y + dy[i];
                        int zVecino = z + dz[i];

                        if(xVecino < 0 || xVecino > 9 || yVecino < 0 || yVecino > 9 || zVecino < 0 || zVecino > 9) {
                            //if neighbor out of the grid, Edge from source(1000)
                            graph.addEdge(1000, new CapacityEdge(from, 1));
                        } else {
                            //neighbor inside => bidirectional edge
                            int to = xVecino * 100 + yVecino * 10 + zVecino;
                            graph.addEdge(from, new CapacityEdge(to, 1));
                        }
                    }
                }
            }
        }

        for(Integer[] defectiveCell: defectiveCells) {
            //if defective, edge to sink with infinite capacity, so that it can't be cut
            int from = defectiveCell[0] * 100 + defectiveCell[1] * 10 + defectiveCell[2];
            graph.addEdge(from, new CapacityEdge(1001, Integer.MAX_VALUE));
        }

        MaximumFlow maximumFlow = new MaximumFlow();
        MaximumFlow.FlowResult flowResult = maximumFlow.edmondsKarp(graph, 1000, 1001);

        System.out.println(flowResult.totalFlow());//maxFlow = mincut
        // (maximumFlow as edges are capacity = 1 is the same as the number of edges I need to cut
        // in order to separate contamination from exterior or sink from source)
    }
}
