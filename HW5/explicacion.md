# Bridges and Tunnels 2

## Problema
Te dan un campus con edificios conectados por caminos que pueden ser interiores (I) o exteriores (O). Para cada consulta (origen, destino), encuentra la ruta óptima.

## Input
- n edificios, m caminos (bidireccionales con tiempo y tipo I/O), p consultas de viajes.

## Output
- Para cada viaje: `tiempo_exterior tiempo_total`, o `IMPOSSIBLE` si no hay ruta.

## Criterio de optimalidad
1. Minimizar tiempo al aire libre.
2. A igualdad de exterior, minimizar tiempo total.

## Solución
Dijkstra modificado donde la distancia es un par `(outdoor, total)` comparado lexicográficamente. La PQ saca siempre el camino con menos outdoor, y a igualdad, menos total. Se ejecuta un Dijkstra por consulta con early exit al llegar al destino.

## Complejidad
- Dijkstra con heap: O((m + n) log n) por consulta.
- Total: O(p · (m + n) log n), con p ≤ 30, n ≤ 4000, m ≤ 40000.

---

# Killing Aliens in a Borg Maze

## Problema
Te dan un laberinto con paredes (#), espacios ( ), aliens (A) y un punto de inicio (S). Hay que encontrar el coste mínimo para que el grupo Borg visite y asimile a todos los aliens. El grupo puede dividirse y el coste es la suma de pasos de todos los subgrupos.

## Input
- N test cases. Cada uno: dimensiones x·y, y el laberinto como grid de caracteres. Máximo 100 aliens, todos alcanzables.

## Output
- Para cada test case: el coste mínimo total.

## Solución
1. **BFS** desde cada nodo importante (S y cada A) para calcular la distancia mínima entre todos los pares de nodos importantes.
2. Con esas distancias, construir un **grafo completo** entre los nodos importantes.
3. Calcular el **MST (Minimum Spanning Tree)** con Prim. El peso del MST es la respuesta.

¿Por qué MST? Porque el grupo puede dividirse en cualquier punto. El MST conecta todos los nodos con el menor coste total, que es exactamente lo que minimiza la suma de distancias recorridas por todos los subgrupos.

## Complejidad
- BFS por nodo importante: O(x · y). Con k nodos importantes: O(k · x · y).
- Prim sobre grafo completo de k nodos: O(k² log k).
- Total: O(k · x · y + k² log k), con k ≤ 101, x·y ≤ 2500.

---

# Escape from Enemy Territory

## Problema
Un grupo de comandos en una cuadrícula X×Y tiene que ir de su posición inicial a un punto de encuentro. Quieren maximizar la distancia mínima a cualquier base enemiga a lo largo de la ruta. A igualdad de separación, eligen la ruta más corta.

## Input
- N bases enemigas, mapa X×Y.
- Posición inicial (xI, yI) y destino (xT, yT).
- N coordenadas de bases enemigas.

## Output
- La separación mínima máxima y la longitud de la ruta.

## Solución
1. **BFS multi-fuente** desde todas las bases enemigas. Calcula `dist[x][y]` = distancia Manhattan a la base más cercana para cada celda.
2. **Búsqueda binaria** sobre D (la separación mínima deseada). Para cada D candidato, se hace un BFS desde inicio a destino usando solo celdas con `dist[x][y] >= D`. Si llega, D es factible y el BFS da la longitud del camino más corto.
3. Se busca el D máximo factible. El BFS correspondiente da la longitud.

## Complejidad
- BFS multi-fuente: O(X · Y).
- Búsqueda binaria: O(log(X + Y)) iteraciones, cada una con un BFS O(X · Y).
- Total: O(X · Y · log(X + Y)), con X, Y ≤ 1000.
