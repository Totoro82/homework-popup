import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class BoardWrapping {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine()); // n test cases

        for (int i = 0; i < n; i++) {
            /// parse input
            int boardsInMould = Integer.parseInt(br.readLine());
            double[][] boards = new double[boardsInMould][5];

            for (int j = 0; j < boardsInMould; j++) {
                StringTokenizer st = new StringTokenizer(br.readLine());

                for (int k = 0; k < 5; k++) {
                    boards[j][k] = Double.parseDouble(st.nextToken());
                }
            }

            /// get all corners
            List<Point> allPoints = new ArrayList<>();
            double totalBoardsArea = 0;

            for (int j = 0; j < boardsInMould; j++) {
                double cx = boards[j][0], cy = boards[j][1];
                double w = boards[j][2], h = boards[j][3];
                double v = boards[j][4];
                totalBoardsArea += w * h;
                allPoints.addAll(List.of(corners(cx, cy, w, h, v)));
            }

            List<Point> convexHull = convexHull(allPoints);
            double polygonArea = polygonArea(convexHull);

            double fraction = totalBoardsArea / polygonArea * 100;
            System.out.printf("%.1f %%\n", fraction);

        }
    }

    static double polygonArea(List<Point> hull) {
        double area = 0;
        int n = hull.size();

        for (int i = 0; i < n; i++) {
            Point p = hull.get(i);
            Point p2 = hull.get((i + 1) % n);
            area += (p.x * p2.y - p2.x * p.y) / 2;
        }
        return area;
    }

    /// cross product of a and b in respect of origin o
    static double crossProduct(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }

    static List<Point> convexHull(List<Point> points) {
        int n = points.size();

        // find anchor: leftmost, if tie: lowest
        int anchorIdx = 0;
        for (int i = 1; i < n; i++) {
            Point p = points.get(i), best = points.get(anchorIdx);
            if (p.x < best.x || (p.x == best.x && p.y < best.y)) anchorIdx = i;
        }
        Collections.swap(points, 0, anchorIdx); // leftmost lowest point at pos0
        final Point p0 = points.get(0);

        // sort by polar angle from p0, ties broken by distance
        points.subList(1, n).sort((a, b) -> {
            double c = crossProduct(p0, a, b);
            // c > 0 => a is closer to cte y ray passing through p0 than b
            // c < 0 => b is closer to cte y ray passing through p0 than a
            if (c != 0) return c > 0 ? -1 : 1;
            // if a & b are on the same direction, closer wins
            // (no sqrt in pythagoras cah only want whos bigger)
            double distanceToA = (a.x - p0.x) * (a.x - p0.x) + (a.y - p0.y) * (a.y - p0.y);
            double distanceToB = (b.x - p0.x) * (b.x - p0.x) + (b.y - p0.y) * (b.y - p0.y);
            return Double.compare(distanceToA, distanceToB);
        });

        // graham scan
        List<Point> hull = new ArrayList<>();
        for (Point p : points) {
            while (hull.size() >= 2 &&
                    //ray from prev2 to prev 1 and if < 0 => cw => delete prev1
                    crossProduct(
                            hull.get(hull.size() - 2),
                            hull.get(hull.size() - 1),
                            p)
                            <= 0) {
                hull.remove(hull.size() - 1);
            }
            hull.add(p);
        }
        return hull;
    }

    static Point[] corners(double cx, double cy, double w, double h, double v) {
        double[][] offsets = { {-w/2, -h/2}, {w/2, -h/2}, {w/2, h/2}, {-w/2, h/2} }; //offsets for each corner
        double vRad = Math.toRadians(v);
        Point[] corners = new Point[4];

        for (int i = 0; i < 4; i++) {
            double dx = offsets[i][0], dy = offsets[i][1];

            //rotate origin with vRad
            double cornerX = dx * Math.cos(vRad) + dy * Math.sin(vRad);
            double cornerY = -dx * Math.sin(vRad) + dy * Math.cos(vRad);
            //translate to actual coordinates (translate to absolute coordinates)
            cornerX += cx;
            cornerY += cy;

            corners[i] = new Point(cornerX, cornerY);
        }
        return corners;
    }

}
class Point {
    double x, y;

    Point(double x, double y) {
        this.x = x; this.y = y;
    }
}
