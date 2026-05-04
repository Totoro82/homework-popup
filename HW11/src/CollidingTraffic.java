import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CollidingTraffic {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        int cases = Integer.parseInt(br.readLine()); // number of test cases

        for (int i = 0; i < cases; i++) {
            /// parse case input
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken()); //boats
            double r = Double.parseDouble(st.nextToken()); // collision distance

            /// parse boats and compute velocity vectors
            double[] vx = new double[n], vy = new double[n];
            double[] x = new double[n], y = new double[n];

            for (int j = 0; j < n; j++) { //for each boat
                st = new StringTokenizer(br.readLine());
                x[j] = Double.parseDouble(st.nextToken());
                y[j] = Double.parseDouble(st.nextToken());
                double d = Double.parseDouble(st.nextToken()); // degrees cw from north
                double dRad = Math.toRadians(d);
                double speed = Double.parseDouble(st.nextToken());
                vx[j] = speed * Math.sin(dRad); // x component
                vy[j] = speed * Math.cos(dRad); // y component
            }

            /// check all pairs
            double best = Double.MAX_VALUE;

            for (int boat1 = 0; boat1 < n; boat1++) {
                for (int boat2 = boat1 + 1; boat2 < n; boat2++) { // for each pair check pairs after it
                    double dx = x[boat1] - x[boat2], dy = y[boat1] - y[boat2];
                    double dvx = vx[boat1] - vx[boat2], dvy = vy[boat1] - vy[boat2];

                    // dist^2(t) = a·t^2 + b·t + c
                    double a = dvx*dvx + dvy*dvy;
                    double b = 2*(dx*dvx + dy*dvy);
                    double c = dx*dx + dy*dy;

                    if (c <= r*r) { best = 0; continue; } // already inside at t=0, collision now
                    if (a == 0) continue; // same velocity, constant distance, they never collide
                    if (b >= 0) continue; // moving apart or at minimum already
                    double disc = b*b - 4 * a * (c - r * r);
                    if (disc < 0) continue; // never reach r

                    double t = (-b - Math.sqrt(disc)) / (2 * a); //- to get t1
                    if (t < best) best = t;
                }
            }

            if (best == Double.MAX_VALUE) out.append("No collision.\n");
            else out.append(Math.round(best))
                    .append('\n');
        }

        System.out.print(out);
    }
}
