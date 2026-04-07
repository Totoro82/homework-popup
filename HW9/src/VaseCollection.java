import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class VaseCollection {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(st.nextToken()); //n test ccases
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int m = Integer.parseInt(st.nextToken());
            int[][] shapeDecoration = new int[37][37]; //1 if mr cheng has it
            for (int j = 0; j < m; j++) {
                st = new StringTokenizer(br.readLine());
                int shape = Integer.parseInt(st.nextToken());
                int decoration = Integer.parseInt(st.nextToken());
                shapeDecoration[shape][decoration] = 1;
            }
            sb.append(findMaxK(shapeDecoration));
            if(i + 1 != n) {
                sb.append("\n");
            }
        }
        System.out.println(sb);
    }

    // try k from max possible (sqrt(100)=10) down, first valid wins
    static int findMaxK(int[][] sd) {
        for (int k = 10; k >= 1; k--) {
            if (tryShapes(sd, k, 1, 0, new int[k], new int[k]))
                return k;
        }
        return 0;
    }

    // pick k distinct shapes via recursion (like nested fors but flexible k)
    static boolean tryShapes(int[][] sd, int k, int start, int depth, int[] shapes, int[] decos) {
        if (depth == k) { // already picked k shapes -> try decorations
            return tryDecos(sd, k, 1, 0, shapes, decos);
        }
        for (int shape = start; shape < 37; shape++) {
            int count = 0; // how many decos this shape has
            for (int d = 0; d < 37; d++) {
                count += sd[shape][d];
            }
            if (count < k) { // can't be part of a kxk square w/ fewer than k decos
                continue;
            }
            shapes[depth] = shape;
            if (tryShapes(sd, k, shape + 1, depth + 1, shapes, decos)) { // shape+1 to avoid repeats
                return true;
            }
        }
        return false;
    }

    // pick k decos that ALL chosen shapes share
    static boolean tryDecos(int[][] sd, int k, int start, int depth, int[] shapes, int[] decos) {
        if (depth == k) return true; // found k valid decos -> kxk square exists
        for (int deco = start; deco < 37; deco++) {
            boolean allHave = true; // check if every chosen shape has this deco
            for (int shapeIdx = 0; shapeIdx < k; shapeIdx++) { // shapeIdx -> index in shapes[]
                if (sd[shapes[shapeIdx]][deco] != 1) {
                    allHave = false;
                    break;
                }
            }
            if (!allHave) continue;
            decos[depth] = deco;
            if (tryDecos(sd, k, deco + 1, depth + 1, shapes, decos)) {
                return true;
            }
        }
        return false;
    }

}
