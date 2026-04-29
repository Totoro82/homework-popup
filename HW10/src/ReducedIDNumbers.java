import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// time : O(G^2) for diffs + sum_d O(sqrt(d)) to mark bad m's, then linear scan
// space: O(maxDiff) for the "bad m" bitset
public class ReducedIDNumbers {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int g = Integer.parseInt(br.readLine());
        int[] sins = new int[g];
        for (int i = 0; i < g; i++) {
            sins[i] = Integer.parseInt(br.readLine());
        }

        // all pairwise abs differences
        int pairs = g * (g - 1) / 2;
        int[] diffs = new int[pairs];
        int k = 0;
        int maxDiff = 0;
        for (int i = 0; i < g; i++) {
            for (int j = i + 1; j < g; j++) {
                int d = Math.abs(sins[i] - sins[j]);
                diffs[k++] = d;
                if (d > maxDiff) maxDiff = d;
            }
        }

        // m is "bad" iff it divides some difference
        // for each diff d enumerate its divisors in O(sqrt(d)) and mark them
        // m > maxDiff is siempre bueno, asique cienpor la respuesta esta en el rango => [G, maxDiff+1].
        boolean[] bad = new boolean[maxDiff + 2];
        for (int d : diffs) {
            for (int x = 1; (long) x * x <= d; x++) {
                if (d % x == 0) {
                    bad[x] = true;
                    bad[d / x] = true;
                }
            }
        }

        // smallest m >= G s.t. m is not bad
        // meaning m < G is impossible (pidgeon hole principle)
        int m = Math.max(1, g);
        while (m < bad.length && bad[m]) {
            m++;
        }
        System.out.println(m);
    }
}
