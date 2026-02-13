import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Cudak {
    static long[][][] memo = new long[16][136][2];
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        String aString = st.nextToken();
        long a = Long.parseLong(aString);
        aString = String.valueOf(a - 1);
        String bString = st.nextToken();
        String sString = st.nextToken();
        int s = Integer.parseInt(sString);

        resetMemo();
        long countA = count(0, 0, 1, aString, s);
        resetMemo();
        long countB = count(0, 0, 1, bString, s);
        System.out.println(countB - countA); // count(0...B) - count(0...A)

        // pad aString with leading zeros to match bString length
        StringBuilder aStringAux = new StringBuilder(String.valueOf(a));
        while (aStringAux.length() < bString.length()) {
            aStringAux.insert(0, "0");
        }
        System.out.println(greedyFirstNumber(aStringAux.toString(), bString, s));
    }


    private static long greedyFirstNumber(String aStringAux, String bString, int s) {
        long result = 0;
        int sumaAcumulada = 0;
        int tight = 1;
        for (int i = 0; i < bString.length(); i++) {
            int maxDigit = (tight == 1) ? bString.charAt(i) - '0' : 9;
            for (int j = aStringAux.charAt(i) - '0'; j <= maxDigit; j++) {
                int newTight = (tight == 1 && j == maxDigit) ? 1 : 0;
                // check if next positions taking this digit can lead to a valid number
                if (count(i + 1, sumaAcumulada + j, newTight, bString, s) > 0) {
                    result = result * 10 + j;
                    sumaAcumulada += j;
                    tight = newTight;
                    break; // smallest valid digit found, move to next position
                }
            }
        }
        return result;
    }

    private static void resetMemo() {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 136; j++)
                Arrays.fill(memo[i][j], -1);
    }

    private static long count(int position, int sumaAcumulada, int tight, String bString, int s) { //tight 1 true, 0 false
        if (position == bString.length()) {
            return (sumaAcumulada == s) ? 1 : 0;
        }
        if (memo[position][sumaAcumulada][tight] != -1) { // if state already recorded skip
            return memo[position][sumaAcumulada][tight];
        }
        long numberCount = 0;
        if (tight == 1) {
            int maxNum = bString.charAt(position) - '0';
            for (int num = 0; num <= maxNum; num++) { // - '0' cause char num values are consecutive
                if(num == maxNum) {
                    numberCount += count(position + 1, sumaAcumulada + num, 1, bString, s);
                } else {
                    numberCount += count(position + 1, sumaAcumulada + num, 0, bString, s);
                }
            }
        } else {
            for (int num = 0; num < 10; num++) {
                numberCount += count(position + 1, sumaAcumulada + num, 0, bString, s);
            }
        }
        memo[position][sumaAcumulada][tight] = numberCount;
        return numberCount;
    }
}
