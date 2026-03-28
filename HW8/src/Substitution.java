import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
///claude --resume f4d35972-3c98-4547-a9d9-ef629f5483d3
/** O(l × 100) por test case.
 - El do-while interno: máximo 100 iteraciones (tamaño del alfabeto)
 - El for externo: l iteraciones (longitud del mensaje, máximo 200)
 - El extGcd: O(log(100)) por llamada, despreciable
 **/
public class Substitution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(st.nextToken());//number of test cases
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());//size of message
            int[] m = new int[l];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < l; j++) {
                m[j] = Integer.parseInt(st.nextToken());
            }

            int[] c = new int[l];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < l; j++) {
                c[j] = Integer.parseInt(st.nextToken());
            }

            int[] p = new int[100];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 100; j++) {
                p[j] = Integer.parseInt(st.nextToken());
            }

//            ///compute E(m)
//            boolean equal = false;
//            int k = 0;
//            while(!equal) {
//                for (int j = 0; j < l; j++) {
//                    m[j] = p[m[j] - 1];
//                }
//                if(Arrays.equals(m, c)) {
//                    equal = true;
//                }
//                k++;
//            }
//            sb.append(k);
//            if(i+1<n) sb.append("\n");
            ///same above but applying CRT(Chinese rest Theorem)
            ///we search for a k that complies both:
            /// k = resultK (mod resultMod)
            /// k = dist (mod cycleLen)
            long resultK = 0;
            long resultMod = 1;
            for (int j = 0; j < l; j++) {
                int val = m[j];
                int dist = -1;
                int steps = 0;

                do {
                    val = p[val -1];
                    steps++;
                    if(val == c[j] && dist == -1) {
                        dist = steps;
                    }
                } while(val != m[j]);

                int cycleLen = steps;

                long[] gxy = extGcd(resultMod, cycleLen);
                long g = gxy[0];// gcd(a, b)
                long x = gxy[1];// coef => resultMod * x + cycleLen * y = gcd(resultMod, cycleLen)
                long diff = dist - resultK;

                long newMod = resultMod / g * cycleLen;

                ///desde resultK, da N saltos de resultMod hasta llegar a un valor que también
                ///   cumpla la nueva ecuación

                // 1. cuántos "saltos efectivos" necesito
                long saltos = diff / g;

                // 2. en qué rango me muevo (el patrón se repite cada esto)
                long rango = cycleLen / g;

                // 3. cuántos saltos reales dar (despejar t multiplicando por el inverso)
                long t = (saltos % rango) * x % rango;

                // 4. desde resultK, doy t saltos de tamaño resultMod
                long newK = resultK + resultMod * t;

                newK = ((newK % newMod) + newMod) % newMod; // force newK to be positive

                resultK = newK;
                resultMod = newMod;
            }
            sb.append(resultK);
            if(i+1<n) sb.append("\n");

        }
        System.out.println(sb);
    }

    /// returns {g,x,y} g is the gcd, the other values are such that
    /// a * x + b * y = gcd(a, b) is correct
    static long[] extGcd(long a, long b) {
        if(b == 0) return new long[] {a, 1, 0};
        long[] r = extGcd(b, a % b); // goes down for extended euclides
        return new long[]{r[0], r[2], r[1] - (a / b) * r[2]}; // goes back up
    }
}
