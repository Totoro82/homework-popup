import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Equations {

    /**
     * Main entry point. Reads test cases and prints the solution for each one.
     *
     * @param args command line arguments (not used)
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            StringTokenizer eq1 = new StringTokenizer(line);
            StringTokenizer eq2 = new StringTokenizer(br.readLine());
            GaussianSolverUnordered gaussianSolverUnordered = new GaussianSolverUnordered(eq1, eq2);
            LinearSystemResult solved = gaussianSolverUnordered.solve();
            if (solved.x == null) sb.append("don't know");
            else if (solved.x[1] == 1) sb.append(solved.x[0]);
            else sb.append(solved.x[0]).append("/").append(solved.x[1]);
            sb.append("\n");

            if (solved.y == null) sb.append("don't know");
            else if (solved.y[1] == 1) sb.append(solved.y[0]);
            else sb.append(solved.y[0]).append("/").append(solved.y[1]);
            sb.append("\n").append("\n");
        }

        System.out.print(sb);
    }
}

class GaussianSolverUnordered {
    StringTokenizer eq1, eq2;

    public GaussianSolverUnordered(StringTokenizer eq1, StringTokenizer eq2) {
        this.eq1 = eq1;
        this.eq2 = eq2;
    }

    LinearSystemResult solve() {
        //arrange as matrix
        GaussianSolver gaussianSolver = matrixArrange(eq1, eq2);
        //call gaussian solver
        return gaussianSolver.solve();
    }

    private GaussianSolver matrixArrange(StringTokenizer eq1, StringTokenizer eq2) {
        long[][] AA = new long[2][2];
        long[] b = new long[2];
        parseEquation(eq1, 0, AA, b);
        parseEquation(eq2, 1, AA, b);
        return new GaussianSolver(AA, b);
    }

    private void parseEquation(StringTokenizer st, int row, long[][] AA, long[] b) {
        int coeffX = 0, coeffY = 0, constSum = 0;
        int side = 1;  // 1 for left of '=', -1 for right
        int sign = 1;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("=")) {
                side = -1;
                sign = 1;
            } else if (token.equals("+")) {
                sign = 1;
            } else if (token.equals("-")) {
                sign = -1;
            } else {
                // it's a term: could be "2x", "-15y", "x", "y", "5", "-3"
                int varIdx = -1; // -1=constant, 0=x, 1=y
                String coeffStr;
                if (token.endsWith("x")) {
                    varIdx = 0;
                    coeffStr = token.substring(0, token.length() - 1);
                } else if (token.endsWith("y")) {
                    varIdx = 1;
                    coeffStr = token.substring(0, token.length() - 1);
                } else {
                    coeffStr = token;
                }

                //extract numeric coefficient:  "x"/"y" means 1,  "-" means -1
                int coeff;
                if (coeffStr.isEmpty() || coeffStr.equals("+")) coeff = 1;
                else if (coeffStr.equals("-")) coeff = -1;
                else coeff = Integer.parseInt(coeffStr);

                //apply side of '=' and operator sign to get the real contribution
                int effective = side * sign * coeff;
                //accumulate into the corresponding variable or constant bucket
                if (varIdx == 0) coeffX += effective;
                else if (varIdx == 1) coeffY += effective;
                else constSum += effective;

                //reset sign to positive for the next term
                sign = 1;
            }
        }
        //we moved everything to the left so coeffX*x + coeffY*y + constSum = 0
        //pass constants to the right side: coeffX*x + coeffY*y = -constSum
        AA[row][0] = coeffX;
        AA[row][1] = coeffY;
        b[row] = -constSum;
    }


}

/**
 * Represents the possible outcomes when solving a linear system.
 */
enum SolveStatus {
    /** The system has exactly one solution. */
    UNIQUE,
    /** The system has infinitely many solutions. */
    MULTIPLE,
    /** The system has no solution. */
    INCONSISTENT
}

/**
 * Encapsulates the result of solving a linear system.
 */
class LinearSystemResult {
    final SolveStatus status;
    final int[] x, y; // {num, den}

    LinearSystemResult(SolveStatus status, int[] x, int[] y) {
        this.status = status;
        this.x = x;
        this.y = y;
    }

    LinearSystemResult(SolveStatus status) {
        this(status, null, null);
    }
}

/**
 * Solves systems of linear equations using Gaussian elimination with partial pivoting.
 * Edited so that it returns integer solutions
 */
class GaussianSolver {
    private final long[][] matrix;
    private final int n;

    GaussianSolver(long[][] A, long[] b) {
        this.n = A.length;
        this.matrix = new long[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, matrix[i], 0, n);
            matrix[i][n] = b[i];
        }
    }

    LinearSystemResult solve() {
        //forward elimination from lab1 PartialLinearEquationSolver, adapted to long[][] and no division
        //pivotRow and col are independent: if a column has no pivot we skip it without advancing pivotRow
        int pivotRow = 0;
        for (int col = 0; col < n && pivotRow < n; col++) {
            //search for largest value of a variable through columns
            int maxRow = pivotRow;
            for (int row = pivotRow + 1; row < n; row++) {
                if (Math.abs(matrix[row][col]) > Math.abs(matrix[maxRow][col])) {
                    maxRow = row;
                }
            }

            //if max is 0, this column has no pivot -> skip without advancing pivotRow
            if (matrix[maxRow][col] == 0) continue;

            //rearrange: set upmost row to the biggest value for the actual variable
            long[] aux = matrix[pivotRow];
            matrix[pivotRow] = matrix[maxRow];
            matrix[maxRow] = aux;

            //multiply row by pivot and substract, no division so everything stays as integers
            for (int row = pivotRow + 1; row < n; row++) {
                long pivotVal = matrix[pivotRow][col];
                long rowVal = matrix[row][col];
                for (int j = col; j <= n; j++) {
                    matrix[row][j] = matrix[row][j] * pivotVal - rowVal * matrix[pivotRow][j];
                }
            }
            pivotRow++;
        }

        //detection from lab1 PartialLinearEquationSolver, same logic
        SolveStatus status = SolveStatus.UNIQUE;
        for (int row = 0; row < n; row++) {
            boolean allZero = true;
            for (int col = 0; col < n; col++) {
                if (matrix[row][col] != 0) {
                    allZero = false;
                    break;
                }
            }
            if (allZero) {
                if (matrix[row][n] != 0) {
                    return new LinearSystemResult(SolveStatus.INCONSISTENT);
                } else {
                    status = SolveStatus.MULTIPLE;
                }
            }
        }

        if (status == SolveStatus.MULTIPLE) {
            //from lab1 PartialLinearEquationSolver solveMultipleSolutions, simplified for 2x2
            //after elimination row 0 is [a, b | c], row 1 is all zeros
            long a = matrix[0][0], b = matrix[0][1], c = matrix[0][n];
            int[] xResult = null, yResult = null;

            if (a != 0 && b == 0) {
                //a*x = c, y is free
                long[] simplifiedX = simplify(c, a);
                if (simplifiedX[1] < 0) { simplifiedX[0] = -simplifiedX[0]; simplifiedX[1] = -simplifiedX[1]; }
                xResult = new int[]{(int) simplifiedX[0], (int) simplifiedX[1]};
            } else if (a == 0 && b != 0) {
                //b*y = c, x is free
                long[] simplifiedY = simplify(c, b);
                if (simplifiedY[1] < 0) { simplifiedY[0] = -simplifiedY[0]; simplifiedY[1] = -simplifiedY[1]; }
                yResult = new int[]{(int) simplifiedY[0], (int) simplifiedY[1]};
            }
            //if both a,b != 0: both depend on each other, neither is determined

            return new LinearSystemResult(SolveStatus.MULTIPLE, xResult, yResult);
        }

        //back-substitution: common denominator is product of diagonals (a*d)
        // [ a  b | c ]    y = e/d,  x = (c - b*y) / a = (c*d - b*e) / (a*d)
        // [ 0  d | e ]    both over den = a*d
        long den = matrix[0][0] * matrix[1][1];
        long xNum = matrix[0][n] * matrix[1][1] - matrix[0][1] * matrix[1][n];
        long yNum = matrix[1][n] * matrix[0][0];

        long[] simplifiedX = simplify(xNum, den);
        long[] simplifiedY = simplify(yNum, den);

        if (simplifiedX[1] < 0) {
            simplifiedX[0] = -simplifiedX[0];
            simplifiedX[1] = -simplifiedX[1];
        }

        if (simplifiedY[1] < 0) {
            simplifiedY[0] = -simplifiedY[0];
            simplifiedY[1] = -simplifiedY[1];
        }

        return new LinearSystemResult(SolveStatus.UNIQUE,
                new int[]{(int) simplifiedX[0], (int) simplifiedX[1]},
                new int[]{(int) simplifiedY[0], (int) simplifiedY[1]});
    }

    /**
     * methods extracted from lab1 Rational Arithmetic
     */

    private long[] simplify(long num, long den) {
        long g = gcd(num, den);
        return new long[]{num / g, den / g};
    }

    private long gcd(long a, long b) {
        if (b == 0) return Math.abs(a);
        return gcd(b, a % b);
    }

}
