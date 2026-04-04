import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Hash {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int mod = 1 << m;// 2 ^ m
        int firstHalf = n/2; // truncated by default
        int secondHalf = n - firstHalf;
//        Map<Integer, Integer> hashCount = new HashMap<>(); // hash(int) -> count(int)
        int[] hashCount = new int[mod]; // hashes from 0 to mod-1

        generateHash(0, 0, firstHalf, mod, hashCount); // generate half the hashes

        long inv33 = modInverse(33, mod);
        long result = reverseHashes(k, 0, secondHalf, mod, inv33, hashCount);

        System.out.println(result);


    }


    static void generateHash(int hash, int depth, int maxDepth, int mod, int[] hashCount) {
        if (depth == maxDepth) {
//            map.merge(hash, 1, Integer::sum);
            hashCount[hash]++;
            return;
        }
        for (int c = 1; c <= 26; c++) {
            int newHash = ((hash * 33) ^ c) % mod;
            generateHash(newHash, depth + 1, maxDepth, mod, hashCount);
        }
    }

    static long reverseHashes(int hash, int depth, int maxDepth, int mod, long inv33, int[] hashCount) {
        if (depth == maxDepth) {
//            return map.getOrDefault(hash, 0);
            return hashCount[hash];
        }

        long count = 0;
        for (int c = 1; c <= 26; c++) {
            int prevHash = (int) (((hash ^ c) * inv33) % mod); // same as hash but in reverse order
            count += reverseHashes(prevHash, depth + 1, maxDepth, mod, inv33, hashCount);
        }

        return count;
    }

    //33 has modInverse because k (final hash value) is always a power of 2 and 33 is odd so gcd(33, 2) = 1
    static long modInverse(long a, long mod) {
        long[] result = extGcd(a, mod);
        return ((result[1] % mod) + mod) % mod;
    }

    static long[] extGcd(long a, long b) {
        if (b == 0) return new long[]{a, 1, 0};
        long[] r = extGcd(b, a % b);
        return new long[]{r[0], r[2], r[1] - (a / b) * r[2]};
    }

//    int hashFunction(String word, int mod) {
//        String substring = word.substring(0, word.length() - 1);
//        if(substring.isEmpty()) return 0;
//
//        char letter = word.charAt(word.length() - 1);
//        int ordLetter = letter - 'a' + 1;
//
//        return ((hashFunction(substring, mod)*33) ^ ordLetter) % mod;
//    }

}
