import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class WhiteWaterRafting {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int cases = Integer.parseInt(br.readLine());

        for (int tc = 0; tc < cases; tc++) {
            // parsear poligono interior
            int innerCount = Integer.parseInt(br.readLine());
            double[] innerX = new double[innerCount];
            double[] innerY = new double[innerCount];
            for (int i = 0; i < innerCount; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                innerX[i] = Double.parseDouble(st.nextToken());
                innerY[i] = Double.parseDouble(st.nextToken());
            }

            // parsear poligono exterior
            int outerCount = Integer.parseInt(br.readLine());
            double[] outerX = new double[outerCount];
            double[] outerY = new double[outerCount];
            for (int i = 0; i < outerCount; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                outerX[i] = Double.parseDouble(st.nextToken());
                outerY[i] = Double.parseDouble(st.nextToken());
            }

            // distancia minima entre los dos poligonos = min sobre pares (vertice, arista)
            double minGap = Double.MAX_VALUE;

            // vertice interior vs arista exterior
            for (int i = 0; i < innerCount; i++) {
                for (int j = 0; j < outerCount; j++) {
                    int next = (j + 1) % outerCount;
                    double dist = pointToSegment(innerX[i], innerY[i],
                            outerX[j], outerY[j], outerX[next], outerY[next]);
                    if (dist < minGap) minGap = dist;
                }
            }

            // vertice exterior vs arista interior
            for (int i = 0; i < outerCount; i++) {
                for (int j = 0; j < innerCount; j++) {
                    int next = (j + 1) % innerCount;
                    double dist = pointToSegment(outerX[i], outerY[i],
                            innerX[j], innerY[j], innerX[next], innerY[next]);
                    if (dist < minGap) minGap = dist;
                }
            }

            // radio max del raft = (gap entre poligonos) / 2
            double radius = minGap / 2.0;
            sb.append(String.format("%.8f%n", radius));
        }
        System.out.print(sb);
    }

    /// distance from p to segment (a, b)
    // proyectamos p sobre la recta a-b y encajamos el parametro t a [0, 1]
    // c = a + t * (b-a) es el pie que buscamos en el segmento
    static double pointToSegment(double px, double py, double ax, double ay, double bx, double by) {
        double dx = bx - ax;
        double dy = by - ay;
        double lenSq = dx * dx + dy * dy; // (b-a)^2

        if (lenSq == 0) { // segment length = 0, not needed in theory cause no hay vertices repetidos consecutivos
            return Math.hypot(px - ax, py - ay);
        }

        double t = ((px - ax) * dx + (py - ay) * dy) / lenSq; //find t
        // force t into [0,1]
        if (t < 0) t = 0;
        else if (t > 1) t = 1;

        double cx = ax + t * dx;
        double cy = ay + t * dy;
        return Math.hypot(px - cx, py - cy);
    }
}
