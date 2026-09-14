package misiones;

import algoritmos.Kruskal;
import estructuras.Edge;
import parser.InputParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mision4 {

    private final Kruskal kruskal;

    public Mision4() {
        kruskal = new Kruskal();
    }

    public String resolverCaso(InputParser parser, int caseNumber) throws IOException {

        int numberOfVertices = parser.nextInt();
        int numberOfConnections = parser.nextInt();

        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < numberOfConnections; i++) {

            int from = parser.nextInt();
            int to = parser.nextInt();
            long weight = parser.nextLong();

            // Input uses vertices from 1 to N.
            // Our Java structures use vertices from 0 to N-1.
            from--;
            to--;

            edges.add(new Edge(from, to, weight));
        }

        long result = kruskal.minimumSpanningTree(
                numberOfVertices,
                edges
        );

        if (result == -1) {
            return "Case #" + caseNumber + ": Limon cut too many cables";
        }

        return "Case #" + caseNumber + ": " + result;
    }
}
