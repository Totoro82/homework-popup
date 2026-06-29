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

            List<AgencyResult> agencyResults = solve(n, m, l, agencyList);

            sb.append("Case " + (i+1) + "\n");

            for (AgencyResult agencyResult: agencyResults) {
                sb.append(agencyResult);
            }

        }
        System.out.println(sb);
    }

    private static List<AgencyResult> solve(int n, int m, int l, List<Agency> agencyList) {
        List<AgencyResult> agencyResultList = new ArrayList<>();
        for(Agency agency: agencyList) {
            int cost = 0;
            int auxN = n;
            while((auxN / 2) >= m && agency.reduceByhalf() < (auxN - auxN/2) * agency.reduceOneUnit()) {
                auxN /= 2;
                cost += agency.reduceByhalf();
            }
            while(auxN != m) {
                auxN--;
                cost += agency.reduceOneUnit();
            }
            agencyResultList.add(new AgencyResult(agency.name(), cost));
        }

        agencyResultList.sort(
                Comparator.comparingInt(AgencyResult::result)
                        .thenComparing(AgencyResult::name)
        );
        return agencyResultList;
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
