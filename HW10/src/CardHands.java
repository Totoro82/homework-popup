import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

// time : O(total cards) — each card -> one HashMap op
// space: O(distinct trie nodes) <= total cards
public class CardHands {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        while (true) {
            String line = br.readLine();
            if (line == null) break;
            int handCount = Integer.parseInt(line.trim());
            if (handCount == 0) break; // terminator

            // trie keyed by (parentNodeId * 64 + cardCode) -> childNodeId
            // root has id 0; created nodes get ids 1..nextId-1
            Map<Long, Integer> trie = new HashMap<>();
            int nextId = 1;

            for (int h = 0; h < handCount; h++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(st.nextToken());
                int[] cards = new int[n];
                for (int i = 0; i < n; i++) cards[i] = encode(st.nextToken());

                // walk the hand reversed (suffix trie) creating nodes only when missing
                int cur = 0;
                for (int i = n - 1; i >= 0; i--) {
                    long key = ((long) cur << 6) | cards[i]; // card fits in 6 bits
                    Integer next = trie.get(key);
                    if (next == null) {
                        next = nextId++;
                        trie.put(key, next);
                    }
                    cur = next;
                }
            }

            out.append(nextId - 1).append('\n');
        }

        System.out.print(out);
    }

    // suit in {C,D,H,S} = 0..3, value in {A,2..9,T/10,J,Q,K} = 0..12
    // canonical code = suit*13 + valueIndex (fits in 6 bits, < 64)
    static int encode(String card) {
        char s = card.charAt(card.length() - 1);
        String valStr = card.substring(0, card.length() - 1);
        int valIdx = switch (valStr) {
            case "A" -> 0;
            case "2" -> 1;
            case "3" -> 2;
            case "4" -> 3;
            case "5" -> 4;
            case "6" -> 5;
            case "7" -> 6;
            case "8" -> 7;
            case "9" -> 8;
            case "10" -> 9;
            case "J" -> 10;
            case "Q" -> 11;
            case "K" -> 12;
            default -> throw new IllegalStateException("bad value: " + valStr); //never enters if input is ok
        };
        int suitIdx = switch (s) {
            case 'C' -> 0;
            case 'D' -> 1;
            case 'H' -> 2;
            case 'S' -> 3;
            default -> throw new IllegalStateException("bad suit: " + s); //never enters if input is ok
        };
        return suitIdx * 13 + valIdx;
    }
}
