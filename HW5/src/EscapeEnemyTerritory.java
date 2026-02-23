import java.util.ArrayDeque;
import java.util.Arrays;

public class EscapeEnemyTerritory {

    // 4-directional movement: down, up, right, left
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in);
        int n = io.getInt();
        int X = io.getInt();
        int Y = io.getInt();

        int xI = io.getInt(), yI = io.getInt(); // initial position
        int xT = io.getInt(), yT = io.getInt(); // target

        int[][] bases = new int[n][2]; // enemy base coordinates
        for (int i = 0; i < n; i++) {
            bases[i][0] = io.getInt();
            bases[i][1] = io.getInt();
        }

        int[][] dist = bfsEnemyDist(bases, X, Y);

        // binary search on D = min separation from enemy bases
        int lo = 0, hi = X + Y; // max possible manhattan distance
        int bestLen = -1;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            int pathLen = bfsPath(dist, xI, yI, xT, yT, mid, X, Y);
            if (pathLen != -1) { // path exists with min separation >= mid
                bestLen = pathLen;
                lo = mid + 1; // try larger separation
            } else {
                hi = mid - 1;
            }
        }

        // hi = best D after binary search
        io.println(hi + " " + bestLen);
        io.close();
    }

    // multi-source BFS: all enemy bases start at dist 0, expands outward
    static int[][] bfsEnemyDist(int[][] bases, int X, int Y) {
        int[][] dist = new int[X][Y];
        for (int[] row : dist) Arrays.fill(row, -1); // -1 = unvisited

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int[] b : bases) {
            dist[b[0]][b[1]] = 0;
            queue.add(b);
        }

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0], cy = curr[1];

            for (int d = 0; d < 4; d++) {
                int nx = cx + dx[d], ny = cy + dy[d];
                if (nx >= 0 && nx < X && ny >= 0 && ny < Y && dist[nx][ny] == -1) {
                    dist[nx][ny] = dist[cx][cy] + 1;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return dist;
    }

    // BFS from start to target, only using cells with enemy dist >= minSep
    // returns path length, or -1 if no path exists
    static int bfsPath(int[][] dist, int xI, int yI, int xT, int yT, int minSep, int X, int Y) {
        if (dist[xI][yI] < minSep || dist[xT][yT] < minSep) return -1; // start or target too close

        // pathDist[x][y] = steps from start, -1 = unvisited
        int[][] pathDist = new int[X][Y];
        for (int[] row : pathDist) Arrays.fill(row, -1);
        pathDist[xI][yI] = 0;

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{xI, yI});

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0], cy = curr[1];

            if (cx == xT && cy == yT) return pathDist[xT][yT]; // reached target

            for (int d = 0; d < 4; d++) {
                int nx = cx + dx[d], ny = cy + dy[d];
                if (nx >= 0 && nx < X && ny >= 0 && ny < Y
                        && pathDist[nx][ny] == -1
                        && dist[nx][ny] >= minSep) { // skip cells too close to enemy
                    pathDist[nx][ny] = pathDist[cx][cy] + 1;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return -1; // target unreachable
    }
}
