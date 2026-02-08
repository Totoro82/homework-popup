# Explicacion de las Implementaciones - HW1

---

## 1. Ferry Loading III

### Problema
Un ferry transporta coches entre dos orillas de un rio. El ferry:
- Puede cargar **n** coches maximo
- Tarda **t** minutos en cruzar
- Empieza en la orilla izquierda
- Carga coches que esperan, priorizando los que llevan mas tiempo
- Si no hay coches esperando en ninguna orilla, espera a que llegue uno

**Objetivo:** Determinar a que minuto llega cada coche a la orilla opuesta.

### Estrategia de Solucion: Simulacion con Colas

```
Estructuras usadas:
- waitingLeft: Cola de coches esperando en la izquierda
- waitingRight: Cola de coches esperando en la derecha
- onFerry: Cola de coches actualmente en el ferry
- results[]: Array para guardar el tiempo de llegada de cada coche
```

### Flujo del Algoritmo

1. **Inicializacion:** El ferry empieza en "left", tiempo = 0

2. **Bucle principal** (mientras haya coches por procesar o esperando):

   a. **Agregar coches llegados:** Todos los coches con `arrivalTime <= time` se anaden a su cola correspondiente

   b. **Cargar ferry:** Se cargan hasta `n` coches de la cola del lado actual (FIFO - primero en llegar, primero en cargar)

   c. **Decidir accion:**
      - Si hay coches en el ferry O esperando en alguna orilla -> **cruzar** (time += t, cambiar posicion)
      - Si no hay coches pero quedan por llegar -> **esperar** (time = tiempo llegada del proximo coche)

   d. **Descargar ferry:** Cada coche descargado registra `results[carIndex] = time`

### Puntos Clave
- Usamos el **indice del coche** en las colas para poder escribir el resultado en el orden original
- La cola garantiza FIFO (First In First Out) = el que lleva mas tiempo esperando sale primero
- El ferry cruza incluso si hay coches esperando en la otra orilla (no en la actual)

---

## 2. Ljutnja (Enfado de los Ninos)

### Problema
Hay **M** caramelos para repartir entre **N** ninos. Cada nino quiere una cantidad especifica. Si un nino recibe menos de lo que quiere, su enfado es el **cuadrado** de los caramelos que le faltan.

**Objetivo:** Minimizar la suma total de enfados.

### Idea Clave: Distribucion Uniforme del "Sufrimiento"

Como el enfado es cuadratico, es mejor que **todos sufran un poco** a que **uno sufra mucho**.

Ejemplo: Si faltan 4 caramelos para 2 ninos:
- Opcion A: Uno sufre 4, otro 0 -> 4^2 + 0^2 = 16
- Opcion B: Ambos sufren 2 -> 2^2 + 2^2 = 8  (MEJOR)

### Estrategia: Greedy con Ordenacion

1. **Ordenar** las necesidades de menor a mayor
2. **Calcular** cuanto sufrimiento total hay que repartir: `remaining = totalNeeds - M`
3. **Iterar** de menor a mayor necesidad:
   - Calcular sufrimiento promedio entre ninos restantes: `suffering = remaining / childrenLeft`
   - Si el nino actual puede "absorber" ese sufrimiento (`suffering <= needs[i]`):
     - Distribuir uniformemente entre todos los restantes
     - Algunos sufriran `suffering + 1` (los del resto de la division)
     - Resultado final y terminar
   - Si no puede (su necesidad es menor que el promedio):
     - Este nino recibe 0 caramelos (sufre todo su `needs[i]`)
     - Restar su sufrimiento del remaining y continuar

### Formulas del Resultado Final
```java
extra = remaining % childrenLeft;  // cuantos sufren +1
result += extra * (suffering+1)^2;  // los que sufren mas
result += (childrenLeft-extra) * suffering^2;  // el resto
```

### Por Que Ordenar?
Los ninos con menos necesidades "saturan" primero (no pueden sufrir mas de lo que piden), asi los sacamos del calculo y distribuimos el resto entre los demas.

---

## 3. Predicting GME

### Problema
Dada una lista de precios de acciones, encontrar el beneficio maximo con restricciones:
- Solo puedes tener 1 accion a la vez
- Solo 1 transaccion por dia
- Despues de vender, **no puedes comprar al dia siguiente** (cooldown)

### Estrategia: Programacion Dinamica con Estados

Definimos 3 estados posibles para cada dia:

| Estado | Significado |
|--------|-------------|
| `hold[i]` | Maximo beneficio si al final del dia i **tenemos** una accion |
| `sold[i]` | Maximo beneficio si **vendimos** en el dia i |
| `rest[i]` | Maximo beneficio si estamos en **cooldown/descanso** (sin accion) |

### Transiciones de Estado

```
Dia i-1          Dia i
---------------------------------
hold[i-1]   ->   hold[i]    (mantener la accion)
rest[i-1]   ->   hold[i]    (comprar hoy: rest - precio)

hold[i-1]   ->   sold[i]    (vender hoy: hold + precio)

rest[i-1]   ->   rest[i]    (seguir descansando)
sold[i-1]   ->   rest[i]    (dia despues de vender = cooldown obligatorio)
```

### Formulas
```java
hold[i] = max(hold[i-1], rest[i-1] - prices[i])  // mantener o comprar
sold[i] = hold[i-1] + prices[i]                   // vender (solo desde hold)
rest[i] = max(rest[i-1], sold[i-1])               // descansar o fin cooldown
```

### Inicializacion (Dia 0)
```java
hold[0] = -prices[0]   // compramos el dia 0
sold[0] = -infinito    // imposible vender sin tener
rest[0] = 0            // no hacer nada
```

### Resultado
```java
max(sold[N-1], rest[N-1])  // no queremos terminar con accion en mano
```

---

## 4. The Trip

### Problema
Un grupo de **n** estudiantes comparte gastos de viaje. Cada uno pago una cantidad diferente. Hay que igualar los costes (con tolerancia de 1 centimo).

**Objetivo:** Calcular el minimo dinero que debe cambiar de manos.

### Idea Clave: Target con Centimo Extra

El coste medio puede no ser exacto, asi que:
- Algunos pagaran `low = total / n` (parte entera)
- Otros pagaran `low + 1` (para absorber el resto)

```java
extraCentSlots = total % n;  // cuantas personas pagan el centimo extra
```

### Estrategia

1. **Convertir todo a centimos** (evitar errores de punto flotante)
   ```java
   spenditures[i] = Math.round(scanner.nextFloat() * 100);
   ```

2. **Calcular low y extraCentSlots**

3. **Contar exceso:** Sumar cuanto han gastado de mas los que superan `low`
   ```java
   spentAboveLow += spenditures[i] - low;  // para cada i donde spenditures[i] > low
   ```

4. **Ajustar por los slots extra:**
   - Algunas personas que gastaron mas de `low` pueden "quedarse" con ese centimo extra
   - Esto reduce el dinero que necesitan devolver
   ```java
   result = spentAboveLow - min(extraCentSlots, pplAboveLow);
   ```

### Por Que Funciona?
- El dinero total que se intercambia = lo que dan los que gastaron de mas
- Pero los que gastaron de mas y pueden pagar `low+1` (en vez de `low`) dan 1 centimo menos
- Solo `min(extraCentSlots, pplAboveLow)` personas se benefician de esto

### Ejemplo
```
3 estudiantes gastaron: $10.00, $20.00, $30.00
Total = 6000 centimos
low = 2000 (cada uno deberia pagar $20)
extraCentSlots = 0

El de $30 gasto 1000 de mas -> debe dar $10.00
El de $10 gasto 1000 de menos -> recibe $10.00
Resultado: $10.00 cambian de manos
```

---

## Resumen Rapido para la Presentacion

| Problema | Tecnica | Complejidad |
|----------|---------|-------------|
| Ferry Loading | Simulacion + Colas | O(m) |
| Ljutnja | Greedy + Ordenacion | O(n log n) |
| Predicting GME | DP con 3 estados | O(n) |
| The Trip | Matematicas + Centimos | O(n) |
