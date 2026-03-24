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

            ///suffixArray[puesto] = posición → "¿quién está en el puesto X?"
            Integer[] suffixArray = new Integer[cadena.length()];
            for(int i = 0; i < cadena.length(); i++) {
                suffixArray[i] = i;
            }
//            Arrays.sort(suffixArray, Comparator.comparing(cadena::substring));

            ///rank[posicion] = puesto → "¿en qué puesto está X?"
            int[] rank = new int[cadena.length()];
            //de primeras le meto de valor su valor de caracter
            for (int i = 0; i < cadena.length(); i++) {
                rank[i] = cadena.charAt(i) - 'a';
            }

            for (int gap = 1; gap < cadena.length(); gap*=2) {
                int finalGap = gap;
                int[] finalRank = rank;
                Arrays.sort(suffixArray, (a, b) -> {
                    if(finalRank[a] != finalRank[b]) return finalRank[a] - finalRank[b];
                    int ra = a + finalGap < cadena.length() ? finalRank[a+finalGap] : -1;
                    int rb = b + finalGap < cadena.length() ? finalRank[b+finalGap] : -1;
                    return ra - rb;
                });

                int[] tmp = new int[rank.length];
                tmp[suffixArray[0]] = 0; //i give rank 0 to the suffix in first place
                for (int i = 1; i < suffixArray.length; i++) {
                    int prev = suffixArray[i -1], cur = suffixArray[i];
                    int prevB = prev + gap < cadena.length() ? rank[prev + gap] : -1;
                    int curB = cur + gap < cadena.length() ? rank[cur + gap] : -1;

                    if (rank[prev] == rank[cur] && prevB == curB) {
                        tmp[cur] = tmp[prev]; // iguales → mismo rank
                    } else {
                        tmp[cur] = tmp[prev] + 1;// distintos → rank + 1
                    }
                }
                rank = tmp;
            }



            // Kasai: LCP in O(n)
            int n = cadena.length();
            int[] lcpArray = new int[n - 1]; // n-1 pares entre n sufijos
            int l = 0; // longitud del prefijo comun actual
            for (int i = 0; i < n; i++) {
                if (rank[i] == 0) { l = 0; continue; } // es el primer sufijo en orden
                int j = suffixArray[rank[i] - 1]; // posicion del sufijo anterior en el orden alfabetico
                while (i + l < n && j + l < n && cadena.charAt(i + l) == cadena.charAt(j + l)) {
                    l++; // cuento cuantos chars comparten
                }
                lcpArray[rank[i] - 1] = l; // guardo el lcp entre el sufijo anterior y este
                if (l > 0) l--; // para el siguiente, empiezo desde l-1
            }

            //sliding window: find longest substring appearing at least m times
            int best = 0;
            int bestPos = -1;
            for (int i = 0; i <= lcpArray.length - (m - 1); i++) {
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