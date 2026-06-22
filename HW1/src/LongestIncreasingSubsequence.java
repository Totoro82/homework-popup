import java.util.Arrays;

public class LongestIncreasingSubsequence {
    public static long solve(long[] arr) {
        int length = arr.length;
        long[] last =  new long[length + 1];
        last[0] = Long.MIN_VALUE;
        Arrays.fill(last, 1, length + 1, Long.MAX_VALUE);

        int[] lastIdx = new int[length + 1];
        int[] parent = new int[length];

        int best = 0;
        for (int i = 0; i < length; i++) {
            int pos = Arrays.binarySearch(last, 0, length, arr[i]);
            if (pos < 0) pos = -(pos + 1);
            last[pos] = arr[i];
            lastIdx[pos] = i;
            parent[i] = lastIdx[pos - 1];

            best = Math.max(best, pos); // pos is actually length of the lis
        }


        long[] result = new long[best];
        int cur = lastIdx[best];
        for (int i = best - 1; i >= 0; i--) {
            result[i] = arr[cur];
            cur = parent[cur];
        }


        System.out.println(Arrays.toString(result));

        return best;
    }

    public static void main(String[] args) {
        System.out.println(solve(new long[]{1, 5, 2, 7, 3, 6}));;
    }

}
