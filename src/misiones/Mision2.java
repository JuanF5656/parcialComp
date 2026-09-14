package misiones;

import algoritmos.Dijkstra;
import algoritmos.Dijkstra.DijkstraResult;
import estructuras.Graph;
import parser.InputParser;

import java.io.IOException;
import java.util.List;

public class Mision2 {

    private final Dijkstra dijkstra = new Dijkstra();

    public CaseResult resolverCaso(InputParser parser, int caseNumber) throws IOException {

        int numberOfVertices = parser.nextInt();
        int numberOfConnections = parser.nextInt();
        int start = parser.nextInt();
        int destination = parser.nextInt();

        Graph graph = new Graph(numberOfVertices);

        for (int i = 0; i < numberOfConnections; i++) {
            int a = parser.nextInt();
            int b = parser.nextInt();
            long weight = parser.nextLong();
            graph.addEdge(a, b, weight);
            graph.addEdge(b, a, weight);
        }

        DijkstraResult result = dijkstra.shortestPath(graph, start, destination);

        String line = result.isReachable()
                ? "Case #" + caseNumber + ": " + result.getDistance()
                : "Case #" + caseNumber + ": Nina is very sad";

        return new CaseResult(line, graph, result.getPath());
    }

    public static class CaseResult {
        private final String outputLine;
        private final Graph graph;
        private final List<Integer> path;

        public CaseResult(String outputLine, Graph graph, List<Integer> path) {
            this.outputLine = outputLine;
            this.graph = graph;
            this.path = path;
        }

        public String getOutputLine() { return outputLine; }
        public Graph getGraph() { return graph; }
        public List<Integer> getPath() { return path; }
    }
}