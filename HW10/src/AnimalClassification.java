import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// time : O(N) per tree (parse iterativo + XOR por hoja)
// space: O(N) for the leaf-hash table and the tree's group set
public class AnimalClassification {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());

        // random 64-bit per leaf id, fixed seed for reproducibility
        // collision prob with N = 100k is ~N^2 / 2^64 << 1
        long[] leafHash = new long[n + 1];
        Random rng = new Random(0xC0FFEEL);
        for (int i = 1; i <= n; i++) leafHash[i] = rng.nextLong();

        Set<Long> alice = collectGroups(br.readLine(), leafHash);
        Set<Long> bob = collectGroups(br.readLine(), leafHash);

        // intersection: walk the smaller, count how many appear in the other
        Set<Long> small = alice.size() <= bob.size() ? alice : bob;
        Set<Long> big = small == alice ? bob : alice;
        int common = 0;
        for (long h : small) if (big.contains(h)) common++;

        System.out.println(common);
    }

    // parse parenthesis-encoded tree iterativamente (no recursion -> no stack overflow w/ deep trees)
    // each '(' pushes a fresh accumulator; each ')' pops and XORs into parent
    // every closed node + every leaf -> register its hash as a "group"
    static Set<Long> collectGroups(String line, long[] leafHash) {
        Set<Long> groups = new HashSet<>();
        Deque<Long> stack = new ArrayDeque<>(); // XOR accumulator per open node
        int i = 0, len = line.length();

        while (i < len) {
            char c = line.charAt(i);
            if (c == '(') {
                stack.push(0L);
                i++;
            } else if (c == ')') {
                long h = stack.pop();
                groups.add(h); // internal node = group

                if (!stack.isEmpty()) stack.push(stack.pop() ^ h); // fold into parent
                i++;
            } else if (c == ',') {
                i++;
            } else if (Character.isDigit(c)) {
                int num = 0;
                while (i < len && Character.isDigit(line.charAt(i))) {
                    num = num * 10 + (line.charAt(i) - '0');
                    i++;
                }
                long h = leafHash[num];

                groups.add(h); // leaf is also a group of size 1
                stack.push(stack.pop() ^ h);
            } else {
                i++; // skip whitespace just in case
            }
        }
        return groups;
    }
}
