import java.util.Scanner;

public class PredictingGME {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int[] predictions = new int[N];
        for (int i = 0; i < N; i++) {
            predictions[i] = scanner.nextInt();
        }
        scanner.close();

        if (N == 1) {
            System.out.println(0);
            return;
        }
        //3 possible states for each day values
        int[] hold = new int[N];
        int[] sold = new int[N];
        int[] rest = new int[N];

        // day 0
        hold[0] = -predictions[0]; //because were not holding nothing day 0
        sold[0] = Integer.MIN_VALUE / 2;  // -inf cause we cant sell day 0
        rest[0] = 0;

        for (int i = 1; i < N; i++) {
            hold[i] = Math.max(hold[i-1], rest[i-1] - predictions[i]);
            sold[i] = hold[i-1] + predictions[i];
            rest[i] = Math.max(rest[i-1], sold[i-1]);
        }

        System.out.println(Math.max(sold[N-1], rest[N-1]));
    }
}
