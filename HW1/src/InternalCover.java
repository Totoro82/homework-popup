import io.Kattio;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InternalCover {
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in);

        int leftBound = io.getInt();
        int rightBound = io.getInt();
        int m = io.getInt();
        int[][] intervals = new int[m][3];
        for (int i = 0; i < m; i++) {
            intervals[i][0] = io.getInt();
            intervals[i][1] = io.getInt();
            intervals[i][2] = i;
        }

        List<Integer> solution = new ArrayList<>();

        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        int i = 0;
        while (rightBound > leftBound) {
            int bestR = leftBound;
            int bestIdx = -1;

            while(i < m && intervals[i][0] <= leftBound) {
                if(intervals[i][1] > bestR) {
                    bestR = intervals[i][1];
                    bestIdx = intervals[i][2];
                }
                i++;
            }

            if(bestIdx == -1) break;
            solution.add(bestIdx);
            leftBound = bestR;
        }

        if (rightBound > leftBound) System.out.println("no solution");
        else System.out.println(solution);
    }
}
