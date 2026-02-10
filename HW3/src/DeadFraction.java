import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class DeadFraction {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        String line;
        while((line = br.readLine()) != null) {
            line = line.trim();
            if(line.equals("0")) break;

            //0.dddd..." -> extract "dddd"
            String digits = line.substring(2, line.length() - 3);
            int len = digits.length();
            long[] bestFraction = new long[]{0, 0};
            for (int i = 0; i < len; i++) {
                int n = i; //non repeating portion length
                int r = len - i; // repeating portion length

                long a = (n > 0) ? Long.parseLong(digits.substring(0, n)) : 0; //non repeating portion
                long b = Long.parseLong(digits.substring(n)); // repeating portion

                long pow10N = 1;
                for (int j = 0; j < n; j++) pow10N *= 10;

                long pow10R = 1;
                for (int j = 0; j < r; j++) pow10R *= 10;

                long leftSideEq = pow10N*pow10R - pow10N; //den
                long righSideEq = (a * pow10R + b) - a; // num

                long[] fraction = simplify(righSideEq, leftSideEq);

                if(i > 0) {
                    if (fraction[1] < bestFraction[1]) { // if den is smaller we update bestDen
                        bestFraction = fraction;
                    }
                } else {
                    bestFraction = fraction;
                }
            }
            sb.append(bestFraction[0]).append("/").append(bestFraction[1]).append("\n");

        }
        System.out.println(sb);
    }

    /**
     * methods extracted from lab1 Rational Arithmetic
     */

    private static long[] simplify(long num, long den) {
        long g = gcd(num, den);
        return new long[]{num / g, den / g};
    }

    private static long gcd(long a, long b) {
        if (b == 0) return Math.abs(a);
        return gcd(b, a % b);
    }

}
