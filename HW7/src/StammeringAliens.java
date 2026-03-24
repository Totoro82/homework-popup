import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class StammeringAliens {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());
        while(m != 0) {
            st = new StringTokenizer(br.readLine());
            String cadena = st.nextToken();
            if (m == 1) {
                System.out.println(cadena.length() + " " + 0);
                st = new StringTokenizer(br.readLine());
                m = Integer.parseInt(st.nextToken());
                continue;
            }
            Integer[] suffixArray = new Integer[cadena.length()];
            for(int i = 0; i < cadena.length(); i++) {
                suffixArray[i] = i;
            }
            Arrays.sort(suffixArray, Comparator.comparing(cadena::substring));

            int[] lcpArray = new int[cadena.length()];
            for (int i = 0; i < cadena.length() - 1; i++) {
                int index = suffixArray[i], index2 = suffixArray[i+1];
                int k = 0;
                while(k < cadena.length() - index && k < cadena.length() - index2 &&
                cadena.charAt(index + k) == (cadena.charAt(index2 + k))) {
                    k++;
                }
                lcpArray[i] = k;
            }

            //sliding window: find longest substring appearing at least m times
            int best = 0;
            int bestPos = -1;
            for (int i = 0; i <= lcpArray.length - m; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 0; j < m - 1; j++) {
                    min = Math.min(min, lcpArray[i + j]);
                }
                if (min > best) {
                    best = min;
                    // rightmost position among the m suffixes in this window
                    int maxPos = 0;
                    for (int j = 0; j < m; j++) {
                        maxPos = Math.max(maxPos, suffixArray[i + j]);
                    }
                    bestPos = maxPos;
                } else if (min == best && best > 0) {
                    // same length, check if there's a more rightward position
                    for (int j = 0; j < m; j++) {
                        bestPos = Math.max(bestPos, suffixArray[i + j]);
                    }
                }
            }

            if (best == 0) {
                System.out.println("none");
            } else {
                System.out.println(best + " " + bestPos);
            }

            st = new StringTokenizer(br.readLine());
            m = Integer.parseInt(st.nextToken());
        }
    }
}