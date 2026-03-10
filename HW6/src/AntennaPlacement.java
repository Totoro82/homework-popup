import util.graph.core.Edge;
import util.graph.core.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

//O(V*E) v being number of * and E being edges btw adjacent *, as main loop tries to enhance matching for every node and dfs goes at most through all edges
public class AntennaPlacement {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int scenarios = Integer.parseInt(st.nextToken());

        for (int i = 0; i < scenarios; i++) {
            st = new StringTokenizer(br.readLine());
            int rows = Integer.parseInt(st.nextToken());
            int columns = Integer.parseInt(st.nextToken());

            int[][] matrix = new int[rows][columns];
            int starCounter = 0;
            for (int j = 0; j < rows; j++) {
                String line = br.readLine();
                for (int k = 0; k < columns; k++) {
                    // 0 if free, 1 if point of interest
                    if(line.charAt(k) == 'o') {
                        matrix[j][k] = 0;
                    } else {
                        matrix[j][k] = 1;
                        starCounter++;
                    }
                }
            }

            Graph<UnweightedEdge> graph = getGraph(rows, columns, matrix);

            int[] matchOf = new int[rows * columns];
            boolean[] visited = new boolean[rows * columns];
            Arrays.fill(matchOf, -1);
            int matching = 0;
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < columns; k++) {
                    if (matrix[j][k] == 1 && (j + k) % 2 == 0) {//we only seek through half of the bipartite graph
                        Arrays.fill(visited, false);
                        if (dfs(j * columns + k, graph, matchOf, visited)) {
                            matching++;
                        }
                    }
                }
            }

            System.out.println(starCounter - matching);
        }
    }

    private static Graph<UnweightedEdge> getGraph(int rows, int columns, int[][] matrix) {
        Graph<UnweightedEdge> graph = new Graph<>(rows * columns);
        //updownleftright representation
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int j = 0; j < rows; j++) {
            for (int k = 0; k < columns; k++) {
                if(matrix[j][k] == 1) {
                    for (int d = 0; d < 4; d++) {
                        int nj = j + dx[d];
                        int nk = k + dy[d];
                        if (nj >= 0 && nj < rows && nk >= 0 && nk < columns && matrix[nj][nk] == 1) { //if inside grid and *
                            graph.addEdge(j * columns + k, new UnweightedEdge(nj * columns + nk));//add edge btw them
                        }
                    }
                }
            }
        }
        return graph;
    }

    static boolean dfs(int node, Graph<UnweightedEdge> graph, int[] matchOf, boolean[] visited) {
        for (UnweightedEdge edge : graph.neighbors(node)) {
            int neighbor = edge.to();
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                //if neighbor hasn't got a match or its current match can find another match
                if (matchOf[neighbor] == -1 || dfs(matchOf[neighbor], graph, matchOf, visited)) {
                    //rematching
                    matchOf[neighbor] = node;
                    matchOf[node] = neighbor;
                    return true;
                }
            }
        }
        return false;
    }

}

record UnweightedEdge(int to) implements Edge {}
