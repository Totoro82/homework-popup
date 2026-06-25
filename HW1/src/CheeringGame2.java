import io.Kattio;

import java.util.Arrays;

public class CheeringGame2 {

    static int n;
    static int[] opp;
    static int[][][][] memo;
    static boolean[][][] visited;

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in);
        n = io.getInt(); // cheerleaders
        int t = io.getInt(); // minutes that a cheerleader can cheer in total
        int m = io.getInt(); // number of active intervals of contrary


        int energy = n * t;
        memo = new int[90][energy + 1][9][2];
        visited = new boolean[90][energy + 1][9];

        int streak = 0; //0 reset, negative values = enemy streak, positive values = our streak

        opp = new int[90];

        for (int i = 0; i < m; i++) {
            int start = io.getInt();
            int end = io.getInt();
            for (int j = start; j < end ; j++) {
                opp[j]++;
            }
        }

        int[] dp = dp(0, energy, 0);

        System.out.println(dp[1] + " " + (dp[1] - dp[0]));

    }

    /// returns [A-B ( which we wanna maximize ), A] for each minute
    private static int[] dp(int minute, int energy, int streak) {
        //base case
        if(minute == 90){
            return new int[]{0,0};
        }

        //halftime reset
        if(minute == 45) {
            streak = 0;
        }

        // best option already calculated?
        if(visited[minute][energy][streak + 4]) {
            return memo[minute][energy][streak + 4];
        }

        int winner, cost;
        int[] loseResult, winResult, tieResult;

        //lose
        if(opp[minute] == 0) {
            loseResult = stateTransition(0, 0, minute, energy, streak);
        } else {
            winner = -1;
            cost = 0;
            loseResult = stateTransition(winner, cost, minute, energy, streak);
        }

        //tie
        if(opp[minute] == 0) {
            tieResult = stateTransition(0, 0, minute, energy, streak);
        } else if(opp[minute] <= n && opp[minute] <= energy) {
            winner = 0;
            cost = opp[minute];
            tieResult = stateTransition(winner, cost, minute, energy, streak);
        } else {
            tieResult = null; //can't tie
        }

        //win
        if((opp[minute] + 1) <= n && opp[minute] + 1 <= energy) {
            winner = 1;
            cost = opp[minute] + 1;
            winResult = stateTransition(winner, cost, minute, energy, streak);
        } else {
            winResult = null; //can't win
        }

        int[] result = bestOption(loseResult, tieResult, winResult);

        memo[minute][energy][streak + 4] = result;
        visited[minute][energy][streak + 4] = true;
        return result;
    }

    private static int[] bestOption(int[] loseResult, int[] tieResult, int[] winResult) {
        if(tieResult == null && winResult == null) return loseResult;
        if(tieResult == null) {
            int compare = Arrays.compare(loseResult, winResult);
            return (compare <= 0) ? winResult : loseResult;
        }
        if(winResult == null) {
            int compare = Arrays.compare(loseResult, tieResult);
            return (compare <= 0) ? tieResult: loseResult;
        }
        int[] bestLoseTie = (Arrays.compare(loseResult, tieResult) <= 0) ? tieResult : loseResult;
        return (Arrays.compare(bestLoseTie, winResult) <= 0) ? winResult : bestLoseTie;
    }

    private static int[] stateTransition(int winner, int cost, int minute, int energy, int streak) {
        int diff, a;
        int[] streakTransition = streakTransition(streak, winner);
        int[] dped = dp(minute + 1, energy - cost, streakTransition[0]);
        int goal = streakTransition[1];
        diff = dped[0] + goal;
        a = dped[1] + (goal > 0 ? 1 : 0); // a only increases with our goals
        return new int[]{diff, a};

    }

    /// returns [newStreak, goals (-/+/0)]
   private static int[] streakTransition(int streak, int winner) {
        if(winner > 0) { // we win
            if(streak >= 0) {
                streak++;
                if((streak % 5) == 0) {
                    streak = 0;
                    return new int[]{streak, 1}; //goal for us
                } else {
                    return new int[]{streak, 0};
                }
            } else {
                streak = 1;
                return new int[]{streak, 0};
            }
        } else if(winner < 0) { //they win
            if(streak <= 0) {
                streak--;
                if((streak % 5) == 0) {
                    streak = 0;
                    return new int[]{streak, -1}; // goal for them
                } else {
                    return new int[]{streak, 0};
                }
            } else {
                streak = -1;
                return new int[]{streak, 0};
            }
        } else { // tie
            streak = 0;
            return new int[]{streak, 0};
        }
    }

}
