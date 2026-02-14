import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Putnik {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int[][] distances = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                distances[i][j] = Integer.parseInt(st.nextToken()); //fill distances matrix
            }
        }
        int[] dp = new int[n+1]; // each value representing minimum cost to go from one side to the other wiht the constructed path
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[1] = 0; //first city, both ends are 1, cost = 0
        for (int i = 2; i <= n; i++) {// i <- la ciudad insertada
            int[] newDp = new int[n+1];
            Arrays.fill(newDp, Integer.MAX_VALUE);
            for (int j = 1; j < i; j++) { //j <- el otro extremo
                if (dp[j] == Integer.MAX_VALUE) continue;

                int goesBefore = distances[i - 1][i - 2]; // cost if append besides i-1
                int goesAfter = distances[i - 1][j - 1]; // cost if append besides j

                // pego i al lado de i-1, la otra punta sigue siendo j, pago vuelo(i, i-1)
                newDp[j] = Math.min(newDp[j], dp[j] + goesBefore);
                // pego i al lado de j, la otra punta pasa a ser i-1, pago vuelo(i, j)
                newDp[i - 1] = Math.min(newDp[i - 1], dp[j] + goesAfter);
            }
            dp = newDp;
        }

        int result = Integer.MAX_VALUE;
        for (int j = 1; j <= n; j++) {
            result = Math.min(result, dp[j]);
        }
        System.out.println(result);
    }
}
