package misiones;

import algoritmos.BellmanFord;
import algoritmos.FloydWarshall;
import estructuras.Edge;
import parser.InputParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mision3 {

    private final FloydWarshall floydWarshall;
    private final BellmanFord bellmanFord;

    public Mision3() {
        floydWarshall = new FloydWarshall();
        bellmanFord = new BellmanFord();
    }

    public String resolverCaso(InputParser parser, int caseNumber) throws IOException {
        int n = parser.nextInt();
        int m = parser.nextInt();
        int source = parser.nextInt();
        int destination = parser.nextInt();

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            edges.add(new Edge(parser.nextInt(), parser.nextInt(), parser.nextLong()));
        }

        FloydWarshall.Result fw = floydWarshall.run(n, edges);
        BellmanFord.Result bf = bellmanFord.run(n, edges, source);

        long fwValue = fw.getMaxChurun()[source][destination];
        boolean fwUnbounded = fw.getUnbounded()[source][destination];

        if (fwValue != FloydWarshall.NO_ROUTE) {
            boolean bfUnreachable = bf.getMaxChurunFromSource()[destination] == BellmanFord.NO_ROUTE;
            if (bfUnreachable) {
                throw new IllegalStateException("Case #" + caseNumber
                        + ": Floyd-Warshall and Bellman-Ford disagree on reachability for D.");
            }
            if (fwUnbounded != bf.getUnbounded()[destination]) {
                throw new IllegalStateException("Case #" + caseNumber
                        + ": Floyd-Warshall and Bellman-Ford disagree on whether D is unbounded.");
            }
            if (!fwUnbounded && fwValue != bf.getMaxChurunFromSource()[destination]) {
                throw new IllegalStateException("Case #" + caseNumber
                        + ": Floyd-Warshall and Bellman-Ford disagree on the maximum churun for D.");
            }
        }

        if (fwValue == FloydWarshall.NO_ROUTE) {
            return "Case #" + caseNumber + ": Limon blocked the way";
        }
        if (fwUnbounded) {
            return "Case #" + caseNumber + ": Infinite churun!";
        }
        return "Case #" + caseNumber + ": " + fwValue;
    }
}