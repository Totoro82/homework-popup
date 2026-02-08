# Explanation HW3

---

## 1. Equations

**Problem:** Given a system of 2 linear equations with variables `x` and `y`, solve the system. Equations come as text with terms separated by `+`, `-` or `=` (e.g. `2x + 3y = x`).

**Output:** Two lines per case: the value of `x` and `y` as reduced fractions. If a variable has no unique value, print `don't know`. Empty line between cases.

**Implementation:**

1. **Parsing** (`parseEquation`): Iterate through tokens of each equation. Track `side` (1=left of `=`, -1=right) and `sign` (+1/-1 depending on operator). For each term, extract coefficient and variable (`x`, `y` or constant). Real contribution = `side * sign * coeff`. Everything ends up as `coeffX*x + coeffY*y = -constSum`.

2. **Gaussian elimination** (adapted from lab1 `PartialLinearEquationSolver`): We use `long[][]` for exact precision. `pivotRow` and `col` are independent (if a column has no pivot, skip it without advancing `pivotRow`). Instead of dividing by the pivot (which would create fractions), we multiply the row by the pivot and subtract: `row[j] = row[j] * pivotVal - rowVal * pivotRow[j]`. This keeps everything as integers.

3. **System type detection**: After elimination, a zero row with `b != 0` is inconsistent. A zero row with `b == 0` is multiple.

4. **MULTIPLE case** (simplified from lab1 `solveMultipleSolutions` for 2x2): Row 0 is `[a, b | c]`. If `a != 0` and `b == 0`, x can be determined (`x = c/a`). If `a == 0` and `b != 0`, y can be determined. If both are != 0, neither variable has a unique value.

5. **UNIQUE case**: Common denominator = `a*d` (product of diagonals). `x = (c*d - b*e) / (a*d)`, `y = (e*a) / (a*d)`. Simplified with GCD (method from lab1 `RationalArithmetic`).

**Complexity:** O(n) per case, where n = number of terms in the equations. Elimination is O(1) since it's always 2x2.

---
