package misiones;

import algoritmos.Dijkstra;
import estructuras.Graph;
import parser.InputParser;

import java.io.IOException;

public class Mision2 {

    private final Dijkstra dijkstra;

    public Mision2() {
        dijkstra = new Dijkstra();
    }

    public String resolverCaso(InputParser parser, int caseNumber) throws IOException {

        int numberOfVertices = parser.nextInt();
        int numberOfConnections = parser.nextInt();
        int start = parser.nextInt();
        int destination = parser.nextInt();

        Graph graph = new Graph(numberOfVertices);

        for (int i = 0; i < numberOfConnections; i++) {

            int a = parser.nextInt();
            int b = parser.nextInt();
            long weight = parser.nextLong();

            // Mission 2 connections are bidirectional
            graph.addEdge(a, b, weight);
            graph.addEdge(b, a, weight);
        }

        long result = dijkstra.shortestPath(graph, start, destination);

        if (result == Long.MAX_VALUE) {
            return "Case #" + caseNumber + ": Nina is very sad";
        }

        return "Case #" + caseNumber + ": " + result;
    }
}