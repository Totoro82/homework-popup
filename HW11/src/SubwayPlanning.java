import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class SubwayPlanning {
    static final double TWO_PI = 2 * Math.PI;
    static final double EPS = 1e-9;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int cases = Integer.parseInt(br.readLine().trim());

        for (int tc = 0; tc < cases; tc++) {
            // parse input
            StringTokenizer st = new StringTokenizer(br.readLine());
            int numPlaces = Integer.parseInt(st.nextToken()); // sitios importantes
            int maxDist = Integer.parseInt(st.nextToken()); // dist max a una estacion

            // buidl angular intervals [theta - alpha, theta + alpha]
            // para los sitios mas lejos que maxDist
            double[] arcStart = new double[numPlaces];
            double[] arcEnd = new double[numPlaces];
            int numArcs = 0;

            for (int i = 0; i < numPlaces; i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                double radius = Math.sqrt((double) x * x + (double) y * y);
                if (radius <= maxDist) continue; // ya cubierto por la propia central station
                double theta = Math.atan2(y, x);
                double halfWidth = Math.asin(maxDist / radius); // semi-ancho del arco de direcciones validas del rayo
                arcStart[numArcs] = theta - halfWidth;
                arcEnd[numArcs] = theta + halfWidth;
                numArcs++;
            }

            int answer = (numArcs == 0) ? 0 : minStabsCircular(arcStart, arcEnd, numArcs);
            out.append(answer).append('\n');
        }
        System.out.print(out);
    }

    // minimo numero de stab points para tocar todos los intervalos circulares [arcStart[i], arcEnd[i]]
    // argumento clasico: alguna sol optima clava en el extremo derecho de algun intervalo
    // → probamos cada arcEnd[i] como primer stab y greedy lineal con el resto
    static int minStabsCircular(double[] arcStart, double[] arcEnd, int numArcs) {
        int best = numArcs; // worst case: 1 stab por intervalo

        double[] shiftedStart = new double[numArcs];
        double[] shiftedEnd = new double[numArcs];
        Integer[] idx = new Integer[numArcs];

        for (int pivotIdx = 0; pivotIdx < numArcs; pivotIdx++) {
            double pivot = arcEnd[pivotIdx];

            // desplazo todos los intervalos para que el pivot caiga en 0
            // tras shift: un intervalo cubre el pivot si "envuelve" el 0 (sa > sb) o si sa == 0
            int remaining = 0;
            for (int j = 0; j < numArcs; j++) {
                double sa = mod2pi(arcStart[j] - pivot);
                double sb = mod2pi(arcEnd[j] - pivot);

                if (sa > sb || sa < EPS) continue; // ya cubierto por el stab del pivot
                shiftedStart[remaining] = sa;
                shiftedEnd[remaining] = sb;
                remaining++;
            }

            // ordenar los restantes por extremo derecho ascendente
            for (int k = 0; k < remaining; k++) {
                idx[k] = k;
            }
            Arrays.sort(idx, 0, remaining, Comparator.comparingDouble(p -> shiftedEnd[p]));

            // greedy lineal: para cada intervalo no cubierto (en orden de end), clavar en su extremo derecho
            int stabs = 1; // el propio pivot
            int k = 0;
            while (k < remaining) {
                double stab = shiftedEnd[idx[k]];
                stabs++;
                while (k < remaining && shiftedStart[idx[k]] <= stab + EPS) {
                    k++;
                }
            }

            if (stabs < best) best = stabs;
        }
        return best;
    }

    /// normaliza un angulo a [0, 2π)
    static double mod2pi(double x) {
        double r = x % TWO_PI;
        if (r < 0) r += TWO_PI;
        return r;
    }
}
