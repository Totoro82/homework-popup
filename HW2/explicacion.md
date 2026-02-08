# Explicacion HW2

---

## 1. Binary Search Tree

**Que pide:** Insertar N numeros distintos en un BST. Tras cada insercion, sumar la profundidad del nodo insertado a un contador C.

**Output:** N lineas, cada una con el valor de C tras insertar ese numero.

**Implementacion:** Usamos un `TreeMap<valor, profundidad>` en vez de construir el arbol real. Al insertar un valor x, su padre en el BST es siempre su predecesor (`lowerKey`) o sucesor (`higherKey`), el que sea mas profundo. La profundidad de x = `max(prof_predecesor, prof_sucesor) + 1`. Acumulamos en `depthCounter` e imprimimos.

**Complejidad:** O(n log n)

---

## 2. Chatter

**Que pide:** Dado n personas y un generador pseudoaleatorio (`r = (r*a+b)%c`), simular n conexiones aleatorias entre pares. Determinar cuantos grupos de chat se forman y sus tamanos.

**Output:** Por cada dia: numero de grupos, seguido de los tamanos en orden descendente. Tamanos duplicados se colapsan como `tamanoXveces` (ej: `3x2`).

**Implementacion:** Union-Find con path compression. Se inicializa `parent[i] = i`. Por cada conexion generada con el PRNG (re-roll si x==y), se hace `union(x,y)`. Al final se recorre `parent[]`, se agrupa por raiz, se cuentan tamanos y se formatean.

**Complejidad:** O(n * alpha(n)) ~ O(n)

---

## 3. Cvjitici

**Que pide:** Cada dia crece una planta con segmento horizontal [L, R] a altura i (dia 1 = altura 1). La planta tiene tallos verticales en x=L y x=R. Cuando un tallo cruza **estrictamente** (no en los extremos) un horizontal de otra planta, nace una flor. Cada posicion (x, y) solo genera una flor.

**Output:** N lineas, cada una con el numero de flores **nuevas** creadas ese dia.

**Implementacion:** Fenwick Tree (BIT) para contar cuantos horizontales cubren cada posicion x. Al registrar un horizontal [L, R], se hace `update(L+1, +1)` y `update(R, -1)` (cobertura estricta, sin extremos). Al plantar una nueva planta, `query(L)` y `query(R)` dan cuantos horizontales previos cruzan esos tallos. Un `HashMap<posicion, conteo_anterior>` evita contar flores duplicadas en la misma columna x.

**Complejidad:** O(n log M), donde M = coordenada maxima (100000)
