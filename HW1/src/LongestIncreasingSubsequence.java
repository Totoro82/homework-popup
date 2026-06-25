import java.util.Arrays;

public class LongestIncreasingSubsequence {
    public static int solve(long[] arr) {
        int length = arr.length;
        long[] last = new long[length + 1]; //last
        last[0] = Long.MIN_VALUE;
        Arrays.fill(last, 1, length + 1, Long.MAX_VALUE);

        int[] lastIdx = new int[length + 1];
        int[] parent = new int[length];

        int best = 0;
        for (int i = 0; i < length; i++) {
            int pos = Arrays.binarySearch(last, 0, length, arr[i]);
            if(pos < 0 ) pos = -(pos + 1);
            last[pos] = arr[i];
            lastIdx[pos] = i;
            parent[i] = lastIdx[pos - 1]; // se indexa por i porque es por elemento no por longitud, pos es actually una longitud

            best = Math.max(best, pos);

        }

        long[] result = new long[best];
        int cur = lastIdx[best];
        for (int i = best - 1; i >= 0 ; i--) {
            result[i] = arr[cur];
            cur = parent[cur];
        }
        System.out.println(Arrays.toString(result));

        return best;

    }

    public static int solveBrute(long[] arr) {
        int length = arr.length;
        int[] len = new int[length];
        int best = 1;

        for (int i = 0; i < length; i++) { // recorro todos los elementos del array
           len[i] = 1;
            for (int j = 0; j < i; j++) { // recorro los anteriores
                if(arr[j] < arr[i]) { // si los anteriores son menores que el elemento que estoy mirando
                    len[i] = Math.max(len[i], len[j] + 1); // entocnes la longitud maxima terminando es la maxima entre la mia actual y la anterior
                }
            }
            best = Math.max(best, len[i]);
        }
        return best;


    }

    public static int solveOrdered(long[] arr) {
        int length = arr.length;
        int best = 1;

        int counter = 1;
        for (int i = 1; i < arr.length; i++) {
            if(arr[i - 1] < arr[i]) {
                    counter++;
                } else {
                    counter = 1;
                }
            best = Math.max(best, counter);
        }

        return best;

    }

    public static void main(String[] args) {
        System.out.println(solveOrdered(new long[]{1, 3,2,4,6}));;
    }
}