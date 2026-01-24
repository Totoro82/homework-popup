import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class CheeringGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // max cheerleaders per minute
        int t = scanner.nextInt(); // total cheerleaders we have
        int m = scanner.nextInt(); // number of enemy intervals
        int[] enemyStrategy = new int[90]; // 90 min match, stores how many cheerleaders enemy has each min
        while (m-- > 0) {
            int lowerBound = scanner.nextInt();  // interval start
            int upperBound = scanner.nextInt() - 1;  // interval end (inclusive)
            for (int i = lowerBound; i <= upperBound; i++) {
                enemyStrategy[i]++; // overlapping intervals stack up
            }
        }
        scanner.close();

        // find 5-min windows where we can outscore enemy, per half separately
        ArrayList<WinnablePeriods> bothHalfs = new ArrayList<>();
        bothHalfs.addAll(findWinnablePeriods(enemyStrategy, n, 0, 45));
        bothHalfs.addAll(findWinnablePeriods(enemyStrategy, n, 45, 90));
        bothHalfs.sort(Comparator.comparingInt(a -> a.totalCost)); // cheapest first so we maximize goals

        // greedily assign cheerleaders to cheapest winnable periods
        int totalCheeringEnergy = n*t; // total budget of cheerleader-minutes
        int[] myStrategy = new int[90]; // how many cheerleaders we place each min
        for(WinnablePeriods period: bothHalfs) {
            if(totalCheeringEnergy >= period.totalCost) {
                for (int i = period.start; i <= period.end ; i++) {
                    myStrategy[i] = enemyStrategy[i]+1; // just enough to beat enemy
                    totalCheeringEnergy-= myStrategy[i];
                }
            }
        }

        // find enemy streaks of 5+ where they score on us
        ArrayList<EnemyStreak> enemyStreaks = new ArrayList<>();
        enemyStreaks.addAll(findEnemyStreaks(enemyStrategy, myStrategy, 0, 45));
        enemyStreaks.addAll(findEnemyStreaks(enemyStrategy, myStrategy, 45, 90));

        // for each streak, find cheapest minute to break every group of 5
        ArrayList<InterruptionPoint> allInterruptionPoints = new ArrayList<>();
        for (EnemyStreak streak : enemyStreaks) {
            allInterruptionPoints.addAll(findOptimalInterruptions(enemyStrategy, n, streak));
        }
        allInterruptionPoints.sort(Comparator.comparingInt(p -> p.cost));

        // spend remaining energy breaking enemy streaks, cheapest first
        for (InterruptionPoint point : allInterruptionPoints) {
            if (totalCheeringEnergy >= point.cost) {
                myStrategy[point.minute] = point.cost;
                totalCheeringEnergy -= point.cost;
            }
        }

        // simulate match with our strategy to count goals
        int[] firstHalf = simulateGoals(myStrategy, enemyStrategy, 0, 45);
        int[] secondHalf = simulateGoals(myStrategy, enemyStrategy, 45, 90);
        int myGoals = firstHalf[0] + secondHalf[0];
        int enemyGoals = firstHalf[1] + secondHalf[1];

        System.out.println(myGoals + " " + enemyGoals);
    }

    // simulates a half: whoever has more cheerleaders wins that minute, 5 consecutive wins = 1 goal
    private static int[] simulateGoals(int[] myStrategy, int[] enemyStrategy, int start, int end) {
        int myGoals= 0;
        int enemyGoals = 0;
        int myStreak = 0;
        int enemyStreak = 0;
        for (int i = start; i < end; i++) {
            if(myStrategy[i] > enemyStrategy[i]) {
                enemyStreak = 0;
                if(++myStreak == 5) { // 5 in a row = goal, reset streak
                    myGoals++;
                    myStreak = 0;
                }
            } else if(enemyStrategy[i] > myStrategy[i]) {
                myStreak = 0;
                if(++enemyStreak == 5) {
                    enemyGoals++;
                    enemyStreak = 0;
                }
            } else { // tie breaks both streaks
                myStreak = 0;
                enemyStreak = 0;
            }
        }
        return new int[]{myGoals, enemyGoals};
    }


    // finds consecutive minutes where enemy beats us, only keeps streaks >= 5 (they can score)
    private static ArrayList<EnemyStreak> findEnemyStreaks(int[] enemyStrategy, int[] myStrategy, int start, int end) {
        ArrayList<EnemyStreak> streaks = new ArrayList<>();
        int streakStart = -1;

        for (int i = start; i < end; i++) {
            boolean enemyWins = enemyStrategy[i] > myStrategy[i];

            if (enemyWins && streakStart == -1) {
                streakStart = i; // streak begins
            } else if (!enemyWins && streakStart != -1) {
                if (i - streakStart >= 5) { // only care if they can score from it
                    streaks.add(new EnemyStreak(streakStart, i - 1));
                }
                streakStart = -1;
            }
        }
        if (streakStart != -1 && end - streakStart >= 5) { // catch streaks that end at half boundary
            streaks.add(new EnemyStreak(streakStart, end - 1));
        }
        return streaks;
    }

    // for each group of 5 in an enemy streak, find the cheapest minute to tie/beat and break the streak
    private static ArrayList<InterruptionPoint> findOptimalInterruptions(int[] enemyStrategy, int n, EnemyStreak streak) {
        ArrayList<InterruptionPoint> optimalInterruptionPoints = new ArrayList<>();
        int streakStart = streak.start;
        int streakEnd = streak.end;

        for (int target = streakStart + 4; target <= streakEnd; target += 5) {
            int bestMinute = -1;
            int bestCost = Integer.MAX_VALUE;

            for (int i = target - 4; i <= target; i++) { // check each min in the group of 5
                if (enemyStrategy[i] <= n && enemyStrategy[i] < bestCost) { // pick cheapest one we can match
                    bestCost = enemyStrategy[i];
                    bestMinute = i;
                }
            }

            if (bestMinute != -1) {
                optimalInterruptionPoints.add(new InterruptionPoint(bestMinute, bestCost));
            }
        }
        return optimalInterruptionPoints;
    }

    // finds 5-min windows where we can beat enemy (enemy < n each min), calculates cost to win them
    private static ArrayList<WinnablePeriods> findWinnablePeriods(int[] enemyStrategy, int n, int start, int end) {
        ArrayList<WinnablePeriods> winnablePeriods = new ArrayList<>();
        int streak = 0;
        for (int i = start; i < end; i++) {
            if(enemyStrategy[i] < n) { // we can beat enemy this minute
                if(++streak == 5) { // found 5 consecutive beatable minutes = we can score
                    int totalCost = 0;
                    for (int j = i - 4; j <= i; j++) {
                        totalCost += enemyStrategy[j] + 1; // need enemy+1 cheerleaders each min
                    }
                    winnablePeriods.add(new WinnablePeriods(i - 4, i, totalCost));
                    streak = 0;
                }
            } else {
                streak = 0; // cant beat this min, reset
            }
        }
        return winnablePeriods;
    }
}

class WinnablePeriods {
    int start; // first minute of the 5-min window
    int end; // last minute of the 5-min window
    int totalCost; // total cheerleaders needed to win this window

    public WinnablePeriods(int start, int end, int totalCost) {
        this.start = start;
        this.end = end;
        this.totalCost = totalCost;
    }
}

class InterruptionPoint {
    int minute; // which minute to place cheerleaders
    int cost; // how many cheerleaders needed to tie that minute

    public InterruptionPoint(int minute, int cost) {
        this.minute = minute;
        this.cost = cost;
    }
}

class EnemyStreak {
    int start; // first minute of enemy winning streak
    int end; // last minute of enemy winning streak

    public EnemyStreak(int start, int end) {
        this.start = start;
        this.end = end;
    }
}