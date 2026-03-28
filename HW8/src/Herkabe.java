import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Herkabe {

    static int MOD = 1000000007;
//    static final int MAX_NODES = 9000000; //3000 x 3000
//    static boolean[] isEnd = new boolean[MAX_NODES];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            names[i] = st.nextToken();
        }
        Arrays.sort(names); // names sharing a prefix end up contiguous

        System.out.println(solve(names, 0, n, 0));

//        int[][] trie = new int[MAX_NODES][26];
//        boolean[] isEnd = new boolean[MAX_NODES];
//        Arrays.fill(trie[0], -1);//root
//        int size = 1;
//
//        //trie creation
//        for (int i = 0; i < n; i++) {
//            st = new StringTokenizer(br.readLine());
//            String name = st.nextToken();
//
//            int node = 0; //always start at root
//
//            for (char c : name.toCharArray()) {
//                int letter = c - 'A';
//                if(trie[node][letter] == -1) {
//                    //unexistent node
//                    trie[node][letter] = size;
//                    Arrays.fill(trie[size], -1);
//                    size++;
//                }
//                node = trie[node][letter];
//            }
//            isEnd[node] = true;
//        }
//
//        System.out.println(dfs(0, trie));

    }

    // O(N * L) where L = max name length, each char visited once across all recursive calls
    // names[start..end) share a common prefix of length depth
    static long solve(String[] names, int start, int end, int depth) {
        long result = 1;
        int childrenCount = 0;
        int i = start;
        while (i < end) {
            if (names[i].length() == depth) {
                childrenCount++; // this name IS the prefix, one more block to permute
                i++;
            } else {
                // group all names sharing the same char at position depth
                char ch = names[i].charAt(depth);
                int j = i + 1;
                while (j < end && names[j].length() > depth && names[j].charAt(depth) == ch) {
                    j++;
                }
                childrenCount++; // this group is one more block
                result = (result * solve(names, i, j, depth + 1)) % MOD; // recurse inside the group
                i = j;
            }
        }
        result = (result * factorial(childrenCount)) % MOD; // permute the blocks
        return result;
    }

//    static long dfs(int node, int[][] trie) {
//        long result = 1;
//        int childrenCount = 0;
//        if(isEnd[node]) childrenCount++; // if node has children and is name, then we can put it before or after them children
//        for (int i = 0; i < trie[node].length; i++) {
//            if(trie[node][i] != -1) childrenCount++; // for every child inside we can permute differently
//        }
//        result = (result * factorial(childrenCount)) % MOD;
//        for (int i = 0; i < trie[node].length; i++) {
//            if(trie[node][i] != -1) {
//                 result = (result * dfs(trie[node][i], trie)) % MOD; // then for every child we run the same
//            }
//        }
//        return result;
//    }

    static long factorial(long n) {
        long result = 1;
        for (int i = 2; i <= n ; i++) {
            result = (result * i) % MOD;
        }
        return result;
    }
}
