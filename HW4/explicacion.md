# Putnik

## Que es dp[j]?

`dp[j]` = lo mas barato que te ha costado construir la fila hasta ahora, sabiendo que una punta es `i` (la ultima ciudad que has metido) y la otra punta es `j`.

## Ejemplo con 4 ciudades

Distancias: 1↔2=15, 1↔3=7, 1↔4=8, 2↔3=16, 2↔4=9, 3↔4=12

---

### Paso 1: pones ciudad 1 en la mesa

```
Mesa: [1]
Coste: 0

dp[1] = 0    →  camino: [1]
```

---

### Paso 2 (i=2): pones ciudad 2

La mesa tiene `[1]`. Las puntas son 1 y 1 (dp[1] = 0).

Pegas 2 a la izquierda o derecha, da igual, cuesta lo mismo:

```
Mesa: [2, 1]   coste: 0 + vuelo(2,1) = 15
      puntas: 2 y 1

dp[1] = 15    →  camino: [2, 1]
```

---

### Paso 3 (i=3): pones ciudad 3

La mesa tiene `[2, 1]`. Las puntas son **2** (=i-1) y **1** (=j). Coste acumulado: 15.

Tienes **dos opciones**:

**Opcion A**: pegas 3 al lado del **2** (la punta i-1):

```
Mesa: [3, 2, 1]    coste: 15 + vuelo(3,2) = 15 + 16 = 31
      puntas: 3 y 1
      → newDp[1] = 31
```

**Opcion B**: pegas 3 al lado del **1** (la punta j):

```
Mesa: [2, 1, 3]    coste: 15 + vuelo(3,1) = 15 + 7 = 22
      puntas: 2 y 3
      → newDp[2] = 22
```

```
dp[1] = 31    →  camino: [3, 2, 1]
dp[2] = 22    →  camino: [2, 1, 3]
```

**Por que guardas el 31 si el 22 es menor?**

Porque son caminos con **puntas distintas**. Mira lo que pasa cuando metes la ciudad 4:

- En `[3, 2, 1]` las puntas son 3 y **1**. La ciudad 4 esta a distancia **8** de la ciudad 1.
- En `[2, 1, 3]` las puntas son 2 y **3**. La ciudad 4 esta a distancia **9** de la 2, y **12** de la 3.

El camino mas caro ahora podria acabar siendo mas barato despues, porque sus puntas podrian estar mas cerca de la siguiente ciudad. **No puedes descartarlo todavia.**

---

### Paso 4 (i=4): pones ciudad 4

Ahora recorres **todos** los j validos:

**j=1** (dp[1]=31, camino `[3, 2, 1]`, puntas: 3 y 1):

```
Opcion A: pegas 4 al lado del 3  →  [4, 3, 2, 1]
  coste: 31 + vuelo(4,3) = 31 + 12 = 43  →  newDp[1] = 43

Opcion B: pegas 4 al lado del 1  →  [3, 2, 1, 4]
  coste: 31 + vuelo(4,1) = 31 + 8 = 39   →  newDp[3] = 39
```

**j=2** (dp[2]=22, camino `[2, 1, 3]`, puntas: 3 y 2):

```
Opcion A: pegas 4 al lado del 3  →  [2, 1, 3, 4]
  coste: 22 + vuelo(4,3) = 22 + 12 = 34  →  newDp[2] = 34

Opcion B: pegas 4 al lado del 2  →  [4, 2, 1, 3]
  coste: 22 + vuelo(4,2) = 22 + 9 = 31   →  newDp[3] = min(39, 31) = 31
                                              ↑ machaca el 39 de antes!
```

```
dp[1] = 43    →  camino: [4, 3, 2, 1]
dp[2] = 34    →  camino: [2, 1, 3, 4]
dp[3] = 31    →  camino: [4, 2, 1, 3]
```

---

### Resultado

min(43, 34, 31) = **31**, camino `[4, 2, 1, 3]`

Verificacion: vuelo(4,2)=9 + vuelo(2,1)=15 + vuelo(1,3)=7 = **31**
