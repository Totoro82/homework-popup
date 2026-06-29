import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class WorkReduction {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int testCases = Integer.parseInt(st.nextToken());

        for (int i = 0; i < testCases; i++) {
            st = new StringTokenizer(br.readLine());

            int n = Integer.parseInt(st.nextToken());// starting workload
            int m = Integer.parseInt(st.nextToken());// your target workload
            int l = Integer.parseInt(st.nextToken());// number of work reduction agencies
            List<Agency> agencyList = new ArrayList<>();

            for (int j = 0; j < l; j++) {
                st = new StringTokenizer(br.readLine(), ": ,");
                String name = st.nextToken();
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                Agency agency = new Agency(name, a, b);
                agencyList.add(agency);
            }

            List<AgencyResult> agencyResults = solve(n, m, agencyList);

            sb.append("Case ").append(i + 1).append('\n');
            for (AgencyResult agencyResult : agencyResults) {
                sb.append(agencyResult);
            }
        }
        System.out.println(sb);
    }

    private static List<AgencyResult> solve(int n, int m, List<Agency> agencyList) {
        List<AgencyResult> results = new ArrayList<>();
        for (Agency agency : agencyList) {
            results.add(new AgencyResult(agency.name(), minCost(agency, n, m)));
        }
        results.sort(
                Comparator.comparingInt(AgencyResult::result)
                        .thenComparing(AgencyResult::name)
        );
        return results;
    }

    private static int minCost(Agency agency, int workload, int target) {
        int cost = 0;
        while (workload / 2 >= target
                && agency.reduceByhalf() < (workload - workload / 2) * agency.reduceOneUnit()) {
            cost += agency.reduceByhalf();
            workload /= 2;
        }
        cost += (workload - target) * agency.reduceOneUnit(); // el resto, de una vez
        return cost;
    }
}

record AgencyResult(String name, int result){
    @Override
    public String toString() {
        return name + ' ' + result + '\n';
    }
}

record Agency (
    String name,
    int reduceOneUnit, int reduceByhalf
) {}
