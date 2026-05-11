import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BoastinRedSocks {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            long p = Long.parseLong(st.nextToken());
            long q = Long.parseLong(st.nextToken());
            if (p == 0 && q == 0) break;

            long g = gcd(p == 0 ? q : p, q);
            long pR = p / g, qR = q / g;

            String result = "impossible";
            for (long n = 2; n <= 50000; n++) {
                long N = n * (n - 1);
                if (N % qR != 0) continue;
                long R = (N / qR) * pR; // must equal r*(r-1)
                long r;

                if (R == 0) {
                    r = 0; // 0 red socks, all black

                } else {
                    long disc = 1 + 4 * R;
                    long s = (long) Math.sqrt(disc);
                    r = -1;

                    for (long t = Math.max(1, s - 1); t <= s + 1; t++) {
                        if (t * t == disc && (1 + t) % 2 == 0) {
                            r = (1 + t) / 2;
                            break;
                        }
                    }
                    if (r < 0 || r > n) continue;
                }
                result = r + " " + (n - r);
                break;
            }
            sb.append(result).append('\n');
        }
        System.out.print(sb);
    }

    static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
