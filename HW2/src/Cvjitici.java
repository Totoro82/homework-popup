import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Cvjitici {

    static int[] bit = new int[100002]; // fenwick tree, coords go up to 100000 + 1 for L+1

    // propagates val upward thru blocks that include pos i
    static void update(int i, int val) {
        for (; i < bit.length; i += i & (-i))
            bit[i] += val;
    }

    // prefix sum 1..i -> how many horizontals strictly contain x=i
    static int query(int i) {
        int sum = 0;
        for (; i > 0; i -= i & (-i))
            sum += bit[i];
        return sum;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(st.nextToken());
        Map<Integer, Integer> lastCount = new HashMap<>(); //how many horizontals contained x last time it changed
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            // subtract prev count to avoid counting flowers that already existed
            int flowersCreatedByL = query(l) - lastCount.getOrDefault(l, 0);
            int flowersCreatedByR = query(r) - lastCount.getOrDefault(r, 0);
            sb.append(flowersCreatedByL+flowersCreatedByR).append("\n");
            // save current count so future stems at same x can deduplicate
            lastCount.put(l, query(l));
            lastCount.put(r, query(r));
            // register horizontal: +1 at L+1 turns on coverage, -1 at R turns it off
            update(l+1, 1);
            update(r, -1);
        }
        System.out.println(sb);
    }

//    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        StringTokenizer st = new StringTokenizer(br.readLine());
//        StringBuilder sb = new StringBuilder();
//
//        TreeSet<Plant> plantTreeSet = new TreeSet<>(Comparator.comparingInt((Plant p) -> p.left).thenComparing(p -> p.height));
//        Set<Flower> flowers = new HashSet<>();
//
//        int n = Integer.parseInt(st.nextToken());
//        int dayBefore = 0;
//
//        for (int i = 0; i < n; i++) {
//            st = new StringTokenizer(br.readLine());
//            plantTreeSet.add(new Plant(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), i+1));
//
//            for (Plant plant : plantTreeSet) {
//                for(Plant other: plantTreeSet) {
//                    if(plant == other) continue;
//                    if(plant.height > other.height) {
//                        if(other.left < plant.left && plant.left < other.right) {
//                            flowers.add(new Flower(plant.left, other.height));
//                        }
//                        if(other.left < plant.right && plant.right < other.right) {
//                            flowers.add(new Flower(plant.right, other.height));
//                        }
//                    }
//                }
//            }
//            sb.append(flowers.size() - dayBefore).append("\n");
//            dayBefore = flowers.size();
//        }
//        System.out.println(sb);
//    }
}
//class Plant {
//    int left, right, height;
//
//    public Plant(int left, int right, int height) {
//        this.left = left;
//        this.right = right;
//        this.height = height;
//    }
//}
//
//class Flower {
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Flower flower = (Flower) o;
//        return x == flower.x && y == flower.y;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(x, y);
//    }
//
//    int x,y;
//
//    public Flower(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//}