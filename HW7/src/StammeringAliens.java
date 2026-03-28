import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 SUFFIX ARRAY + LCP
 * =====================
 * Un suffix array es un array con los indices de todos los sufijos de un string, ordenados alfabeticamente.
 * Esto permite hacer busqueda binaria sobre los sufijos para encontrar cualquier substring en O(log n).

 * suffixArray[puesto] = posicion → "en el puesto X del orden alfabetico, que sufijo esta?"
 * rank[posicion] = puesto → "el sufijo que empieza en la posicion X, en que puesto va?"
 * Son lo inverso: suffixArray te dice quien esta en cada puesto, rank te dice en que puesto esta cada uno.

 * DOUBLING TRICK (construccion del suffix array en O(n log^2 n)):
 * ================================================================
 * En vez de comparar sufijos enteros (O(n) por comparacion → O(n^2 log n) total),
 * ordenas incrementalmente duplicando la longitud que comparas en cada paso.

 * 1. Inicio: rank[i] = valor del caracter en posicion i. Solo codifica la primera letra.
 *    Las letras solo se "usan" aqui. A partir de aqui todo es comparar ranks (numeros).

 * 2. Loop con gap = 1, 2, 4, 8, ... (log n pasos, como un binary search pero al reves):
 *    Para ordenar por los primeros 2*gap caracteres, comparas el par (rank[i], rank[i+gap]):
 *    - rank[i] → orden de las primeras 'gap' letras (ya lo calculaste en el paso anterior)
 *    - rank[i+gap] → orden de las siguientes 'gap' letras. Este es el truco: la segunda mitad
 *      de tu sufijo ES EL INICIO DE OTRO SUFIJO, y su rank ya lo tienes calculado. Gratis.

 *    Ejemplo con gap=2: quieres ordenar "anana" por sus primeros 4 chars "anan":
 *    - rank[1] codifica "an" (2 letras, calculado en gap=1)
 *    - rank[1+2] = rank[3] codifica "an" (las 2 letras a partir de pos 3, tambien de gap=1)
 *    - Juntos: (rank[1], rank[3]) codifica "anan" (4 letras). Cada comparacion es O(1).

 * 3. Despues de cada sort, actualizas ranks recorriendo suffixArray en orden:
 *    - Si el par (rank[prev], rank[prev+gap]) == (rank[cur], rank[cur+gap]) → mismo rank.
 *      Significa que con la info que tenemos no podemos distinguir estos dos sufijos aun.
 *    - Si el par es distinto → rank + 1.
 *    - Cuando todos los ranks son unicos, ya terminaste.

 * LCP ARRAY:
 * ===========
 * lcp[i] = cuantas letras comparten suffixArray[i] y suffixArray[i+1] (sufijos consecutivos en el orden).
 * Si dos sufijos consecutivos tienen un LCP alto, significa que el string original contiene un
 * mismo trozo de texto que aparece en dos posiciones distintas.
 * El substring compartido es: cadena.substring(suffixArray[i], suffixArray[i] + lcp[i])

 * SLIDING WINDOW (encontrar substring mas largo que aparece al menos m veces):
 * ==============================================================================
 * Si m sufijos consecutivos en el suffix array comparten un prefijo largo, ese substring
 * aparece m veces. El minimo del LCP en una ventana de m-1 es lo que comparten TODOS
 * los m sufijos (el cuello de botella es el par que menos comparte).
 * El maximo de todos esos minimos es la respuesta.

 * Complejidad total: O(n log^2 n) para suffix array + O(n^2) para LCP + O(n*m) para sliding window.
 **/
public class StammeringAliens {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());
        while(m != 0) {
            st = new StringTokenizer(br.readLine());
            String cadena = st.nextToken();
            if (m == 1) {
                System.out.println(cadena.length() + " " + 0);
                st = new StringTokenizer(br.readLine());
                m = Integer.parseInt(st.nextToken());
                continue;
            }

            ///suffixArray[puesto] = posición → "¿quién está en el puesto X?"
            Integer[] suffixArray = new Integer[cadena.length()];
            for(int i = 0; i < cadena.length(); i++) {
                suffixArray[i] = i;
            }
//            Arrays.sort(suffixArray, Comparator.comparing(cadena::substring));

            ///rank[posicion] = puesto → "¿en qué puesto está X?"
            int[] rank = new int[cadena.length()];
            //de primeras le meto de valor su valor de caracter
            for (int i = 0; i < cadena.length(); i++) {
                rank[i] = cadena.charAt(i) - 'a';
            }

            // Doubling trick: gap se duplica cada vez (1, 2, 4, 8...) → log n pasos
            for (int gap = 1; gap < cadena.length(); gap*=2) {
                int finalGap = gap;
                int[] finalRank = rank;
                // Ordeno suffixArray comparando pares (rank[a], rank[a+gap]) vs (rank[b], rank[b+gap])
                // rank[a] = orden de las primeras 'gap' letras del sufijo a
                // rank[a+gap] = orden de las siguientes 'gap' letras (es el rank del sufijo que empieza ahi)
                // Si no hay segunda mitad (sufijo corto), -1 para que vaya antes (como en el diccionario)
                Arrays.sort(suffixArray, (a, b) -> {
                    if(finalRank[a] != finalRank[b]) return finalRank[a] - finalRank[b];
                    int ra = a + finalGap < cadena.length() ? finalRank[a+finalGap] : -1;
                    int rb = b + finalGap < cadena.length() ? finalRank[b+finalGap] : -1;
                    return ra - rb;
                });

                // Actualizo ranks: recorro suffixArray ya ordenado y asigno nuevo rank.
                // prev = posicion original del sufijo anterior en el orden
                // cur = posicion original del sufijo actual
                // rank[prev] vs rank[cur] = ¿sus primeras 'gap' letras son iguales?
                // prevB vs curB = ¿sus siguientes 'gap' letras son iguales?
                // Si ambas iguales → no podemos distinguirlos aun → mismo rank
                // Si cualquiera distinta → rank + 1
                int[] tmp = new int[rank.length];
                tmp[suffixArray[0]] = 0; //i give rank 0 to the suffix in first place
                for (int i = 1; i < suffixArray.length; i++) {
                    int prev = suffixArray[i -1], cur = suffixArray[i];
                    int prevB = prev + gap < cadena.length() ? rank[prev + gap] : -1;
                    int curB = cur + gap < cadena.length() ? rank[cur + gap] : -1;

                    if (rank[prev] == rank[cur] && prevB == curB) {
                        tmp[cur] = tmp[prev]; // iguales → mismo rank
                    } else {
                        tmp[cur] = tmp[prev] + 1;// distintos → rank + 1
                    }
                }
                rank = tmp;
            }



            // Kasai: LCP in O(n)
            int n = cadena.length();
            int[] lcpArray = new int[n - 1]; // n-1 pares entre n sufijos
            int l = 0; // longitud del prefijo comun actual
            for (int i = 0; i < n; i++) {
                if (rank[i] == 0) { l = 0; continue; } // es el primer sufijo en orden
                int j = suffixArray[rank[i] - 1]; // posicion del sufijo anterior en el orden alfabetico
                while (i + l < n && j + l < n && cadena.charAt(i + l) == cadena.charAt(j + l)) {
                    l++; // cuento cuantos chars comparten
                }
                lcpArray[rank[i] - 1] = l; // guardo el lcp entre el sufijo anterior y este
                if (l > 0) l--; // para el siguiente, empiezo desde l-1
            }

            // Sliding window: substring mas largo que aparece al menos m veces.
            // m sufijos consecutivos → m-1 LCPs entre ellos.
            // El minimo del LCP en la ventana = lo que comparten TODOS los m sufijos
            // (el cuello de botella es el par que menos comparte, como el eslabon mas debil).
            // El maximo de todos esos minimos = la respuesta.
            // Si best==0 → ningun substring aparece m veces → "none".
            int best = 0;
            int bestPos = -1;
            for (int i = 0; i <= lcpArray.length - (m - 1); i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 0; j < m - 1; j++) {
                    min = Math.min(min, lcpArray[i + j]);
                }
                if (min > best) {
                    best = min;
                    // Los m sufijos de la ventana son suffixArray[i..i+m-1].
                    // Cada uno es una posicion en el string original donde aparece el substring.
                    // Nos quedamos con la mas a la derecha (la mayor), porque el enunciado la pide.
                    int maxPos = 0;
                    for (int j = 0; j < m; j++) {
                        maxPos = Math.max(maxPos, suffixArray[i + j]);
                    }
                    bestPos = maxPos;
                } else if (min == best && best > 0) {
                    // misma longitud, miramos si hay posicion mas a la derecha
                    for (int j = 0; j < m; j++) {
                        bestPos = Math.max(bestPos, suffixArray[i + j]);
                    }
                }
            }

            if (best == 0) {
                System.out.println("none");
            } else {
                System.out.println(best + " " + bestPos);
            }

            st = new StringTokenizer(br.readLine());
            m = Integer.parseInt(st.nextToken());
        }
    }
}