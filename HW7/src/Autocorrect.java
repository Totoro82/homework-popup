import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;

///complexity L word length
/// O(L) go down through trie
/// DP is O(L iterations and O(L) inside to calculate k so thats O(L^2)
/// trie constructions is O(sum of lengths in dictionary)
public class Autocorrect {

    static final int MAX_NODES = 1000000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());//words in dictionary
        int m = Integer.parseInt(st.nextToken());// words to type
        StringBuilder sb = new StringBuilder();

        // trie[j][m] es desde el nodo j si sigo la letra m llego al nodo ...?
        int[][] trie = new int[MAX_NODES][26];
//        for (int i = 0; i < MAX_NODES; i++) {
//            Arrays.fill(trie[i], -1);
//        }
        Arrays.fill(trie[0], -1);
        String[] suggestion = new String[MAX_NODES];//suggestion for each node
        int size = 1;//acts as node id

        //creation of trie dictionary
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
                if(suggestion[node] == null) { //set suggestion
                    suggestion[node] = word; //first word that comes through = most frequent
                }
            }
        }

        //ir palabra a palabra letra a letra bajando por el trie para ver lo que sugiere
        //me quedo con el minimo para cada palabra entre el minimo de la sugerencia en cualquier
        // punto de la palabra y el coste en si de la propia palabra
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            String word = st.nextToken();
            int node = 0;

            int[] dpMinKeystrokes = new int[word.length() + 1];//0 chars written - all written
            Arrays.fill(dpMinKeystrokes, Integer.MAX_VALUE);
            dpMinKeystrokes[0] = 0;

//            for(char c : word.toCharArray()) {
//                int letter = c - 'a';
//                if(trie[node][letter] == -1) break; // no possible suggestion
//                node = trie[node][letter];
//                writtenSoFar++;
//                String actualSuggestion = suggestion[node];
//
//                int k = 0;
//                while(k < word.length() && k < actualSuggestion.length() &&
//                    word.charAt(k) == actualSuggestion.charAt(k)) {
//                    k++;
//                }
//                // lo que llevo escrito mas el tab mas lo que tengo que borrar mas lo que tengo
//                // que escribir dsps de la sugerencia
//                int cost = writtenSoFar + 1 + (actualSuggestion.length() - k) + (word.length() - k);
//                minKeystrokes = Math.min(minKeystrokes, cost);
//            }

            String prevSuggestion = null;
            int k = 0;
            for (int j = 0; j < word.length(); j++) {
                //write letter
                dpMinKeystrokes[j+1] = Math.min(dpMinKeystrokes[j+1], dpMinKeystrokes[j] + 1);

                //tab if node exists?
                if(j >= 1 && node != -1) {
                    String actualSuggestion = suggestion[node];

                    if(!Objects.equals(actualSuggestion, prevSuggestion)) {
                        k = j;
                        while(k < word.length() && k < actualSuggestion.length() &&
                                word.charAt(k) == actualSuggestion.charAt(k)) {
                            k++;
                        }
                        prevSuggestion = actualSuggestion;
                    }
                    // lo que llevo escrito mas el tab mas lo que tengo que borrar mas lo que tengo
                    // que escribir dsps de la sugerencia
                    int cost = dpMinKeystrokes[j] + 1 + (actualSuggestion.length() - k);
                    dpMinKeystrokes[k] = Math.min(dpMinKeystrokes[k], cost);
                }

                if(node != -1) {
                    int letter = word.charAt(j) - 'a';
                    node = trie[node][letter];
                }
                
            }
            
            sb.append(dpMinKeystrokes[word.length()]).append("\n");
        }

        System.out.println(sb);

    }
}
