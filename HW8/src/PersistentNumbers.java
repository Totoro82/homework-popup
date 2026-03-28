import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class PersistentNumbers {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        StringBuilder sb = new StringBuilder();
        while (!(line = br.readLine().trim()).equals("-1")) {
            BigInteger number = new BigInteger(line);

            ///edge cases
            if (number.equals(BigInteger.ZERO)) {
                sb.append("10").append("\n");
                continue;
            }
            if (number.equals(BigInteger.ONE)) {
                sb.append("11").append("\n");
                continue;
            }

            ArrayList<Integer> factorized = factorizeNumber(number);

            if(factorized.isEmpty()) {
                sb.append("There is no such number.").append("\n");
                continue;
            }

            factorized.sort(Comparator.comparingInt(a -> a));
            if (factorized.size() == 1) {
                sb.append("1");
            }
            for (int digit : factorized) {
                sb.append(digit);
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private static ArrayList<Integer> factorizeNumber(BigInteger number) {
        ArrayList<Integer> result = new ArrayList<>();
        while(number.compareTo(BigInteger.ONE) > 0 ) {
            boolean found = false;
            for (int i = 9; i > 1; i--) {
                if(number.mod(BigInteger.valueOf(i)).equals(BigInteger.valueOf(0))) {
                    number = number.divide(BigInteger.valueOf(i));
                    result.add(i);
                    found = true;
                    break;
                }
            }
            if(!found) {
                result.clear();
                break;
            }
        }
        return result;
    }


}
