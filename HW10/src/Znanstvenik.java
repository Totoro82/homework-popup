import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

// time : O(R*C*log R)  -- log R bcause each builds C strings of length <=R
// space: O(R*C)
public class Znanstvenik {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        char[][] grid = new char[r][];
        for (int i = 0; i < r; i++) {
            grid[i] = br.readLine().toCharArray();
        }

        // binary search largest k in [0, r-1] s.t columns of grid[k..r-1] are all different
        int lo = 0, hi = r - 1, ans = 0;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;// unsigned shift, 0s are insertedd

            if (allColumnsDistinct(grid, mid, c)) { // ok -> try to remove more
                ans = mid;
                lo = mid + 1;
            } else { // collision -> remove fewer
                hi = mid - 1;
            }
        }

        System.out.println(ans);
    }

    // build each column as a String over rows [startRow..R-1] and dump into a set
    // distinct iff set.size == c
    static boolean allColumnsDistinct(char[][] grid, int startRow, int c) {
        int rows = grid.length;
        Set<String> seen = new HashSet<>(c * 2); // pre size to avoid rehashing
        char[] buf = new char[rows - startRow];

        for (int col = 0; col < c; col++) {
            for (int row = startRow; row < rows; row++){
                buf[row - startRow] = grid[row][col];
            }
            if (!seen.add(new String(buf))) return false; // duplicate found, early return
        }
        return true;
    }
}
