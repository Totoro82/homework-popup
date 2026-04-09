import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Zagrade {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[] expression = br.readLine().toCharArray();
        Deque<Integer> lifo = new ArrayDeque<>();
        int[][] parenthesisPairs = new int[10][2]; // {start, end}
        int parenthesisPairsCount = 0;
        // locate the parenthesis pairs
        for (int i = 0; i < expression.length; i++) {
            char c = expression[i];
            if(c == '(') {
                lifo.push(i);
            } else if (c == ')') {
                parenthesisPairs[parenthesisPairsCount][0] = lifo.pop();
                parenthesisPairs[parenthesisPairsCount][1] = i;
                parenthesisPairsCount++;
            }
        }

        Set<String> treeSet = new TreeSet<>();
        // mask from 1`because we need to remove at least 1 pair
        // for every possible combination of removing parenthesis
        for (int mask = 1; mask < (1 << parenthesisPairsCount); mask++) {
            Set<Integer> skip = new HashSet<>();
            // for every pair of parenthesis
            for (int i = 0; i < parenthesisPairsCount; i++) {
                // if bit is 1, we remove that parenthesis
                if((mask & (1 << i)) != 0) {
                    skip.add(parenthesisPairs[i][0]);
                    skip.add(parenthesisPairs[i][1]);
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < expression.length; j++) {
                if(!skip.contains(j)) {
                    sb.append(expression[j]);
                }
            }
            treeSet.add(sb.toString());
        }

        for (String s : treeSet) {
            System.out.println(s);
        }
    }
}
