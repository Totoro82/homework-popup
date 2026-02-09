import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PerfectPthPowers {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        while(true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long x = Long.parseLong(st.nextToken());
            if(x == 0) break; // eof
            long absX = Math.abs(x); //abs is long cause abs(minInt) overflows integer
            for (int th = 31; th > 0; th--) { // we use 32 cause if x is an int it will be max 2^31-1, x<=2^31 and th <= log2(|x|), so th <= 31
                if(x < 0 && th % 2 == 0) continue; // a negative number can only happen with a negative base and an odd exp
                double root = Math.pow(absX, 1.0 / th);
                long p = Math.round(root);
                double pow = Math.pow(p, th);
                if(Math.abs(pow - absX) < 0.5) { // as they are both integers, always at a minimum distance of 1 or equal
                    sb.append(th).append("\n");
                    break;
                }
            }
        }
        System.out.println(sb);
    }
}
