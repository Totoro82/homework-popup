import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class XOREquation {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " xor=");
        String a = st.nextToken();
        String b = st.nextToken();
        String c = st.nextToken();

        char[][] arrays = {a.toCharArray(), b.toCharArray(), c.toCharArray()};

        // contar ?s por numero para elegir cual deducir
        int[] qCount = new int[3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < arrays[i].length; j++)
                if (arrays[i][j] == '?') qCount[i]++;

        // deducir el que tenga mas ?s → swap a posicion 2
        int deduceId = 0;
        if (qCount[1] > qCount[deduceId]) deduceId = 1;
        if (qCount[2] > qCount[deduceId]) deduceId = 2;
        if (deduceId != 2) {
            char[] tmp = arrays[deduceId];
            arrays[deduceId] = arrays[2];
            arrays[2] = tmp;
        }

        // recoger solo los ?s de arrays[0] y arrays[1] (los que iteramos)
        int[][] qMarks = new int[10][2]; // {arrayId, idx}
        int numQAB = 0;
        for (int i = 0; i < arrays[0].length; i++)
            if (arrays[0][i] == '?') qMarks[numQAB++] = new int[]{0, i};
        for (int i = 0; i < arrays[1].length; i++)
            if (arrays[1][i] == '?') qMarks[numQAB++] = new int[]{1, i};

        System.out.println(findDistinctSolutions(arrays, qMarks, numQAB, 0));
    }

    private static int findDistinctSolutions(char[][] arrays, int[][] qMarks, int numQAB, int qIdx) {
        if(qIdx == numQAB) {
            int va = Integer.parseInt(new String(arrays[0]));
            int vb = Integer.parseInt(new String(arrays[1]));
            int xor = va ^ vb;

            String xorStr = Integer.toString(xor);
            // c tiene que tener la misma longitud
            if (xorStr.length() != arrays[2].length) return 0;
            // los digitos fijos de c tienen que coincidir
            for (int i = 0; i < arrays[2].length; i++) {
                //para cada char de c
                // si es distinto de ? en el original
                // y no coincide con el de despues de xor esta combinacion ya no vale
                if (arrays[2][i] != '?' && arrays[2][i] != xorStr.charAt(i)) return 0;
            }
            return 1;
        }
        int result = 0;
        for (int d = 0; d <= 9; d++) {

            //check for leading 0's
            // si el indice en el que estoy del array en el que estoy es 0
            // y el array tiene mas de un numero
            // y d es 0
            if(qMarks[qIdx][1] == 0 && arrays[qMarks[qIdx][0]].length > 1 && d == 0) {
                continue;
            }

            arrays[qMarks[qIdx][0]][qMarks[qIdx][1]] = (char) ('0' + d);
            result += findDistinctSolutions(arrays, qMarks, numQAB, qIdx + 1);
        }
        return result;
    }

}
