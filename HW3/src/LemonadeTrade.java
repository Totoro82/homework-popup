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
        if(n == 0) { System.out.printf("%.15f%n", 0.0); return; }
        Trade[] trade = new Trade[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            trade[i] = new Trade(st.nextToken(), st.nextToken(), Double.parseDouble(st.nextToken()));
        }
        TradeSolver tradeSolver = new TradeSolver(n, trade);
        System.out.printf("%.15f%n", tradeSolver.solve());
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

class TradeSolver {
    final int n;
    final Trade[] trades;

    public TradeSolver(int n, Trade[] trades) {
        this.n = n;
        this.trades = trades;
    }

    double solve() {
        HashMap<String, Double> dp = new HashMap<>();
        dp.put("pink", 1.0); // initially we have 1 liter of pink lemonade
        for(Trade trade: trades) {
            double quantityWanted = dp.getOrDefault(trade.wanted, 0.0);
            double quantityOffered = quantityWanted * trade.exchangeRate;
            dp.put(trade.offered, Math.max(quantityOffered, dp.getOrDefault(trade.offered, 0.0)));
        }
        return Math.min(10.0, dp.getOrDefault("blue", 0.0));
    }
}