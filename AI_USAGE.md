# AI_USAGE.md

## Herramientas utilizadas y en qué partes
- Claude (chat): se usó desde el inicio para definir la estructura general
  del proyecto (separar el código en paquetes `algoritmos`, `estructuras`,
  `misiones`, `parser` y `UI`, y mantener los algoritmos independientes de
  la interfaz gráfica) y los pasos básicos a seguir para no intentar todo
  a la vez. 

## Prompts decisivos
    1. "¿Cómo deberiamos organizar la estructura del proyecto?" — usado al 
    empezar, ayudó a dividir el trabajo pendiente en pasos concretos
    (algoritmo → clase de la misión → panel de interfaz) en vez de encarar todo junto.

    2. "Que mejorarías de la visualización de la misión 2, para poder recibir input
    y mostrarlo" — este prompt definió el patrón que terminamos repitiendo
    en las cuatro misiones (entrada, salida, botón de ejemplo, ejecutar,
    selector de casos), lo cual nos dio consistencia sin tener que
    rediseñar cada panel desde cero.

## Casos donde el resultado generado fue incorrecto o subóptimo
1. `GraphPanel` inicialmente ubicaba los nodos con un centro y un radio
   fijos en el código. Esto hacía que el grafo se viera descentrado o
   cortado dentro del panel hasta agrandar la ventana. Lo detectamos
   corriendo la aplicación y se corrigió calculando el centro y el radio
   a partir del tamaño real del panel (`getWidth()`/`getHeight()`) en
   lugar de valores fijos.

## Qué aprendió cada integrante
    Juan Felipe: Aprendí la razón de por qué el algoritmo de dijkstra
    es utilizado para situaciones como la misión 2 que requiere encontrar
    la distancia más pequeña de un nodo a otro utilizando PriorityQueue.
    Para situaciones como la misión 4 en donde se requiere utilizar la menor 
    cantidad de cable para reconstruir una red, kruskal organiza cada cable 
    por costo y utilizando Union-Find verifica si los puntos finales pertenecen
    al mismo componente, sino, se permite la union; si sí se cancela por que crearía
    un ciclo. Se para a los N-1 cables aceptados y si no se alcanzan los N-1
    quiere decir que el grafo no estaba conectado del todo.

    Thomas Gonzalez: Aprendi que el BFS es para encontrar los 
    caminos mas cortos 
    mientas que el DFS es para encontrar los
    mas largos para encontrar las salidas, estos dos algoritmos usando
    FIFO, por otro lado tambien vi que el bellman ford es mas lento que el dijkstra 
    pero el ford tolera negativos y detecta ciclos.
    
