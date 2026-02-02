import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class BinarySearchTree {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        TreeMap<Integer, Integer> treeMap = new TreeMap<>(); // value -> depth
        StringBuilder sb = new StringBuilder();
        long depthCounter = 0;

        for (int i = 0; i < n; i++) {
            int value = Integer.parseInt(br.readLine());
            if (i == 0) {
                treeMap.put(value, 0); // root is always depth 0
            } else {
                // parent of new node is always its predecessor or successor (whichever is deeper)
                Integer lo = treeMap.lowerKey(value); // largest value smaller than x
                Integer hi = treeMap.higherKey(value); // smallest value larger than x
                int depth = 0;
                if (lo != null) depth = treeMap.get(lo) + 1;
                if (hi != null) depth = Math.max(depth, treeMap.get(hi) + 1);
                treeMap.put(value, depth);
                depthCounter += depth;
            }
            sb.append(depthCounter).append('\n');
        }
        System.out.print(sb);
    }
}
