import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Zadaca {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int[] aFactors = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            aFactors[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());
        int[] bFactors = new int[m];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            bFactors[i] = Integer.parseInt(st.nextToken());
        }

        Map<Integer, Integer> mapAPrimeFactors = factorize(aFactors);
        Map<Integer, Integer> mapBPrimeFactors = factorize(bFactors);

        long result = 1;
        boolean big = false;
        for (int prime: mapAPrimeFactors.keySet()) {
            if (mapBPrimeFactors.containsKey(prime)) {
                int minExp = Math.min(mapAPrimeFactors.get(prime),
                        mapBPrimeFactors.get(prime));
                for (int i = 0; i < minExp; i++) {
                    result *= prime;
                    if (result >= 1000000000) {
                        big = true;
                        result %= 1000000000;
                    }
                }
            }
        }

        if(big) {
            System.out.printf("%09d%n", result);
        } else {
            System.out.println(result);
        }

    }

    static private Map<Integer, Integer> factorize(int[] factors) {
        Map<Integer, Integer> primeExp = new HashMap<>();
        for (int num: factors) { // recorro los factores
            for (int i = 2; i * i <= num ; i++) { // recorro hacia arriba para sus posibles primos
                while(num % i == 0) {
                    primeExp.merge(i, 1, Integer::sum); //if not there key value, if there key, value+1
                    num /= i;
                }
            }
            if(num > 1) { //sqrt(n) reached and still smth left that != 1, then its a prime number
                primeExp.merge(num, 1, Integer::sum);
            }
        }
        return primeExp;
    }

}
