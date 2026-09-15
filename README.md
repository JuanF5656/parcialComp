Listo, aquí está con los dos integrantes y sus responsabilidades reales según lo que hicimos:

```markdown
# The Feline Graph Chronicles

Proyecto de curso **Lenguajes y Compiladores** — Universidad EIA. Aplicación de escritorio en Java 17 con interfaz Swing que resuelve las cuatro misiones sobre grafos del enunciado, con los seis algoritmos implementados desde cero.

## Integrantes

| Persona | Nombre | Responsabilidad |
|---|---|---|
| Persona A | Juan Felipe Atehortúa | Misión 1 (BFS y DFS), Misión 3 (Floyd-Warshall, Bellman-Ford, verificación cruzada), paneles de dibujo de grilla y grafo dirigido |
| Persona B | Thomas González Aguilar | Misión 2 (Dijkstra), Misión 4 (Kruskal), armazón de la interfaz gráfica, repositorio |

Ambos integrantes son responsables de la entrega completa y pueden explicar y modificar cualquier parte del proyecto.

## Cómo compilar y ejecutar

Con un solo comando desde un clon limpio:

```bash
./run.sh
```

`run.sh` compila el proyecto a `./out` si hace falta y abre la interfaz gráfica (`Main`). Para compilar sin ejecutar:

```bash
./build.sh
```

**Requisitos:** JDK 17 o superior. El proyecto no usa Maven ni Gradle — solo `javac`/`java`, así que no depende de ningún repositorio externo para compilar.

También se puede abrir la carpeta con IntelliJ IDEA y ejecutar la clase `Main`.

## Estructura del proyecto

```
src/
  Main.java                    Punto de entrada, abre la ventana principal

  algoritmos/                  Los seis algoritmos, sin ningún import de Swing
    BFS.java                   Misión 1
    DFS.java                   Misión 1 (pila explícita, ver "Decisiones")
    Dijkstra.java               Misión 2
    FloydWarshall.java          Misión 3
    BellmanFord.java             Misión 3 (verificación cruzada + reconstrucción de ruta/ciclo)
    Kruskal.java                Misión 4

  estructuras/                 Tipos de datos compartidos
    Position.java                Celda (fila, columna) de la grilla de Misión 1
    Edge.java                    Arista (origen, destino, peso)
    Graph.java                    Lista de adyacencia, usada por Misiones 2, 3 y 4
    UnionFind.java                Disjoint set con path compression y unión por tamaño

  misiones/                    Parsing del input + formato de salida "Case #k: ..."
    Mision1.java, Mision2.java, Mision3.java, Mision4.java
    (cada una expone un CaseResult con la línea de salida y los datos
     que la interfaz necesita para dibujar)

  parser/
    InputParser.java              Lee el input como flujo de tokens separados
                                   por espacios en blanco, tolerando líneas en
                                   blanco y espacios extra (sección 2.2)

  UI/                           Interfaz gráfica (Swing), sin lógica de negocio
    MainWindow.java               Ventana principal, una pestaña por misión
    Mision1Panel.java, Mision2Panel.java, Mision3Panel.java, Mision4Panel.java
    GraphPanel.java                Dibuja grafos (Misiones 2, 3 y 4)
    GridPanel.java                 Dibuja la grilla con bombas (Misión 1)
```

Cada algoritmo vive en una clase sin ningún import de `javax.swing`, así que se puede instanciar y correr `BFS`, `Dijkstra`, `Kruskal`, etc. directamente desde una prueba, sin abrir ninguna ventana.

## Decisiones tomadas

**DFS iterativo con pila explícita, no hilo con pila ampliada.** El enunciado permite cualquiera de las dos opciones. Se eligió la pila explícita porque no depende de configurar el tamaño de stack de la JVM ni de cómo lo maneje cada sistema operativo: cada celda guarda un contador `nextDirection` que recuerda cuál de las 4 direcciones (arriba, abajo, izquierda, derecha) le toca intentar a continuación, jugando el mismo papel que la variable local de una llamada recursiva. Esto reproduce exactamente el orden de exploración de una recursión up/down/left/right, sin usar el call stack de Java — necesario porque las grillas llegan a 10⁶ celdas.

**Sin librerías de grafos externas.** El núcleo algorítmico está implementado desde cero, como exige la sección 2.1 del enunciado. La única librería usada para dibujar es `java.awt.Graphics2D`, que viene con el JDK.

**Dijkstra usa `java.util.PriorityQueue`**, no un escaneo O(N²), tal como exige el enunciado para N hasta 10.000.

**Kruskal se apoya en un `UnionFind` propio** con path compression y unión por tamaño, por lo que cada `find`/`union` corre en tiempo amortizado casi constante; el costo real queda dominado por ordenar las aristas (O(C log C)).

**Floyd-Warshall y Bellman-Ford se corren juntos en cada caso de la Misión 3** y se comparan entre sí: si el valor de Bellman-Ford para el destino no coincide con la matriz de Floyd-Warshall, o disienten sobre si el destino es alcanzable o "unbounded", `Mision3` lanza una excepción en vez de imprimir una respuesta que podría estar mal. La interfaz atrapa esa excepción y la muestra como "Cross-check mismatch" en lugar de fallar en silencio. Bellman-Ford además guarda el predecesor de cada nodo durante la relajación, para poder reconstruir la ruta óptima o extraer el ciclo de ganancia positiva responsable y resaltarlo en el dibujo.

**El grafo de la Misión 3 es dirigido.** El panel de dibujo original usaba una clave de arista simétrica pensada para las Misiones 2 y 4, que son no dirigidas — eso habría confundido una arista 1→2 con su opuesta 2→1. Se agregó un modo dirigido con flechas y clave de arista ordenada, sin tocar el comportamiento que ya usaban las Misiones 2 y 4.

**La Misión 1 no tiene un total de casos al inicio del input**, a diferencia de las otras tres: termina con un caso centinela `R=0 C=0`. Por eso recorre el input en un solo bucle interno y devuelve todos los casos de una vez, en vez de resolver un caso por llamada desde un `for` externo.

## Límites de dibujo respetados (sección 2.3)

| Misión | Límite de dibujo obligatorio | Qué pasa si se excede |
|---|---|---|
| 1 | grillas hasta 50×50 | se omite el dibujo, se muestra un mensaje y la respuesta numérica sigue visible |
| 2 | hasta 60 nodos | ídem |
| 3 (grafo) | hasta 60 nodos | ídem |
| 3 (matriz N×N) | hasta N = 100 | se omite la tabla, se muestra un mensaje |
| 4 | hasta 100 intersecciones / 300 cables | ídem |

