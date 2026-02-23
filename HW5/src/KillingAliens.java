import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class KillingAliens {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine().trim());

        StringBuilder sb = new StringBuilder();

        for (int tc = 0; tc < t; tc++) {
            // skip blank lines between test cases
            String dimLine = br.readLine();
            while (dimLine != null && dimLine.trim().isEmpty()) {
                dimLine = br.readLine();
            }

            String[] dims = dimLine.trim().split("\\s+");
            int cols = Integer.parseInt(dims[0]);
            int rows = Integer.parseInt(dims[1]);

            char[][] maze = new char[rows][cols]; // parsed input maze
            List<int[]> nodes = new ArrayList<>(); // S and A positions

            for (int r = 0; r < rows; r++) {
                String line = br.readLine();
                for (int c = 0; c < cols && c < line.length(); c++) {
                    maze[r][c] = line.charAt(c);
                }
                // pad w/ spaces if line shorter than cols (trailing spaces get trimmed)
                for (int c = line.length(); c < cols; c++) {
                    maze[r][c] = ' ';
                }
            }

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (maze[r][c] == 'S' || maze[r][c] == 'A') {
                        nodes.add(new int[]{r, c});
                    }
                }
            }

            int n = nodes.size();
            int[][] dist = new int[n][n]; // shortest path between each pair of important nodes
            int[] dr = {-1, 1, 0, 0}; // row offset -> up, down, left, right
            int[] dc = {0, 0, -1, 1}; // col offset -> up, down, left, right

            // bfs from each important node to get distances for the mst graph
            for (int i = 0; i < n; i++) {
                int[][] bfsDist = new int[rows][cols];
                for (int[] row : bfsDist) Arrays.fill(row, -1); // -1 = unvisited

                Queue<int[]> queue = new LinkedList<>();
                int sr = nodes.get(i)[0], sc = nodes.get(i)[1];
                bfsDist[sr][sc] = 0;
                queue.add(new int[]{sr, sc});

                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    for (int d = 0; d < 4; d++) {
                        int nr = cur[0] + dr[d];//next row
                        int nc = cur[1] + dc[d];//next column

                        //if inside the maze and no wall and not visited yet
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                && maze[nr][nc] != '#' && bfsDist[nr][nc] == -1) {
                            bfsDist[nr][nc] = bfsDist[cur[0]][cur[1]] + 1;
                            queue.add(new int[]{nr, nc});
                        }
                    }
                }

                for (int j = 0; j < n; j++) {
                    dist[i][j] = bfsDist[nodes.get(j)[0]][nodes.get(j)[1]];
                }
            }

            // build complete graph w/ bfs distances as weights, then mst
            Graph graph = new Graph(n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        graph.addEdge(i, j, dist[i][j]);
                    }
                }
            }

            Optional<Prim.MinimumSpanningTree> result = Prim.prim(graph);
            sb.append(result.get().totalWeight()).append('\n');//no checking in get because problem states that all aliens are reachable
        }

        System.out.print(sb);
    }
}
