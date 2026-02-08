import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class LemonadeTrade {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());

        if(n == 0) { System.out.printf("%.7f\n", 0.0); return; }
        Trade[] trades = new Trade[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            trades[i] = new Trade(st.nextToken(), st.nextToken(), Double.parseDouble(st.nextToken()));
        }

        TradeSolver tradeSolver = new TradeSolver(n, trades);
        System.out.printf("%.7f\n", tradeSolver.solve());
    }
}


class Trade {
    final String offered, wanted;
    final double exchangeRate;

    public Trade(String offered, String wanted, double exchangeRate) {
        this.offered = offered;
        this.wanted = wanted;
        this.exchangeRate = exchangeRate;
    }
}

record TradeSolver(int n, Trade[] trades) {
    double solve() {
        HashMap<String, Double> dp = new HashMap<>();
        dp.put("pink", 0.0); // log(1.0) = 0
        for (Trade trade : trades) {
            double logWanted = dp.getOrDefault(trade.wanted, Double.NEGATIVE_INFINITY);
            double logOffered = logWanted + Math.log(trade.exchangeRate);
            dp.put(trade.offered, Math.max(logOffered, dp.getOrDefault(trade.offered, Double.NEGATIVE_INFINITY)));
        }
        double logBlue = dp.getOrDefault("blue", Double.NEGATIVE_INFINITY);
        if (logBlue == Double.NEGATIVE_INFINITY) return 0.0;
        if (logBlue > Math.log(10.0)) return 10.0;
        return Math.exp(logBlue);
    }
}