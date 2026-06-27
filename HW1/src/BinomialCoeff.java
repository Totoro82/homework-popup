import io.Kattio;

import java.util.Arrays;

public class BinomialCoeff {
    public static void main(String[] args) {
        Kattio kattio = new Kattio(System.in);
        //n choose k
        int n = kattio.getInt();
        int k = kattio.getInt();

        int[][] cache = new int[n+1][k+1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(cache[i], -1);
        }

        System.out.println(binCoef(n, k, cache));
    }

    private static int binCoef(int n, int k, int[][] cache) {
        if(k == 0 || k == n) {
            return 1;
        }
        if(cache[n][k] == -1) {
            cache[n][k] = binCoef(n-1, k-1, cache) + binCoef(n-1, k, cache);
        }
        return cache[n][k];
    }
}
