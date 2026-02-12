import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Assassins {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        double[] states = new double[1 << n]; // 2^n states (2^15 max)
        states[(1 << n) - 1] = 1.0;
        for (int i = 0; i < m; i++) { //for each person
            st = new StringTokenizer(br.readLine());
            int attacker = Integer.parseInt(st.nextToken());
            int victim = Integer.parseInt(st.nextToken());
            double prob = Double.parseDouble(st.nextToken());

            for (int j = 0; j < (1 << n); j++) { //go through all states
                if (states[j] == 0) continue; // optimization, if all dead no assassination can happen
                boolean attackerIsAlive = (j & (1 << (attacker - 1))) != 0;
                boolean victimIsAlive = (j & (1 << (victim -1))) != 0;
                if(attackerIsAlive && victimIsAlive) {
                    int stateAfterVictimsDeath = j & ~(1 << (victim - 1)); // j but with the victim bit turned off (he dead)
                    states[stateAfterVictimsDeath] += states[j] * prob;
                    states[j] *= (1 - prob);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) { //for each person
            double alive = 0;
            for (int j = 0; j < (1 << n); j++) { // go thorugh all states
                if((j & (1 << i)) != 0) {
                    alive += states[j];
                }
            }
            sb.append(String.format("%.7f", alive)).append("\n");
        }
        System.out.println(sb);
    }
}
