import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Crabbles {

    ///complexity O(26^P) for dfs and O(N*L) l length, for trie construct

    static int best;
    static int[][] trie;
    static boolean[] isEnd;
    static int[][] tileScores; // tileScores[letter][i] = i-th best value for each letter (if available)
    static int[] tileCount;    // how many tiles of letter

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); //words in dictionary

        isEnd = new boolean[2500001];
        trie = new int[2500001][26];
        Arrays.fill(trie[0], -1);
        int size = 1;//acts as node id

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            String word = st.nextToken();

            int node = 0; //always start at root

            for(char c : word.toCharArray()) {
                int letter = c - 'a';
                if(trie[node][letter] == -1) {
                    //unexistent node
                    trie[node][letter] = size;
                    Arrays.fill(trie[size], -1);
                    size++;
                }
                node = trie[node][letter];// I go down to corresponding node
            }
            isEnd[node] = true;
        }

        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());//number of hands
        tileScores = new int[26][10]; // max 10 tiles per hand
        tileCount = new int[26];
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int p = Integer.parseInt(st.nextToken());//number of tiles

            Arrays.fill(tileCount, 0);
            for (int j = 0; j < p; j++) {
                st = new StringTokenizer(br.readLine());
                int letter = st.nextToken().charAt(0) - 'a';
                int value = Integer.parseInt(st.nextToken());
                tileScores[letter][tileCount[letter]++] = value;
            }

            //sort letter values ascending and read from the end in dfs
            for (int l = 0; l < 26; l++) {
                if (tileCount[l] > 1) Arrays.sort(tileScores[l], 0, tileCount[l]);
            }

            //play hand with DFS
            best = 0;
            int[] used = new int[26];
            dfs(used, 0, 0);
            sb.append(best).append('\n');
        }
        System.out.print(sb);
    }

    static void dfs(int[] used, int node, int score) {
        if (isEnd[node]) {
            best = Math.max(best, score);
        }

        for (int l = 0; l < 26; l++) {
            if (trie[node][l] == -1) continue;
            if (used[l] >= tileCount[l]) continue;

            int value = tileScores[l][tileCount[l] - 1 - used[l]];
            used[l]++;
            dfs(used, trie[node][l], score + value);
            used[l]--;
        }
    }
}
