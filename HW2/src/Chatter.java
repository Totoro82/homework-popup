import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Chatter {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line = br.readLine()) != null) {
            Map<Integer, Integer> groupSizes = new HashMap<>(); // root -> group size
            StringTokenizer st = new StringTokenizer(line);
            int n = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken()); // random seed
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int[] parent = new int[n]; // union-find, person -> group root
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
            // simulate n random chat connections
            for (int i = 0; i < n; i++) {
                int x, y;
                do { // reroll if same person
                    r = (r * a + b) % c;
                    x = r % n;
                    r = (r * a + b) % c;
                    y = r % n;
                } while(x == y);
                union(x,y, parent);
            }
            for (int i = 0; i < n; i++) {
                int root = find(i, parent);
                groupSizes.put(root, groupSizes.getOrDefault(root, 0) + 1);
            }
            sb.append(groupSizes.size());
            List<Integer> sizes = groupSizes.values().stream().sorted((h, q) -> q - h).toList(); // descending order
            // collapse duplicate sizes into "sizexcount" format
            int i = 0;
            while (i < sizes.size()) {
                int count = 1;
                while(i+count < sizes.size() && sizes.get(i+count).equals(sizes.get(i))) {
                    count++;
                }
                sb.append(" ").append(sizes.get(i));
                if(count > 1) {
                    sb.append("x").append(count);
                }
                i += count;
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }


    static void union(int x, int y, int[] parent) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        if(rootX != rootY) {
            parent[rootX] = rootY;
        }
    }

    static int find(int node, int[] parent) {
        if (parent[node] != node) {
            parent[node] = find(parent[node], parent); // compress path to root
        }
        return parent[node];
    }
}
