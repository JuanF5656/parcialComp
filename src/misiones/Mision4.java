package misiones;

import algoritmos.Kruskal;
import algoritmos.Kruskal.KruskalResult;
import estructuras.Edge;
import estructuras.Graph;
import parser.InputParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mision4 {

    private final Kruskal kruskal = new Kruskal();

    public CaseResult resolverCaso(InputParser parser, int caseNumber) throws IOException {

        int numberOfVertices = parser.nextInt();
        int numberOfConnections = parser.nextInt();

        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < numberOfConnections; i++) {
            int from = parser.nextInt();
            int to = parser.nextInt();
            long weight = parser.nextLong();

            // Input is 1-indexed, our structures are 0-indexed
            from--;
            to--;

            edges.add(new Edge(from, to, weight));
        }

        KruskalResult result = kruskal.minimumSpanningTree(numberOfVertices, edges);

        String line = result.isConnected()
                ? "Case #" + caseNumber + ": " + result.getTotalCost()
                : "Case #" + caseNumber + ": Limon cut too many cables";

        // Display graph = every candidate cable, so discarded ones show up too
        Graph displayGraph = new Graph(numberOfVertices);
        for (Edge edge : edges) {
            displayGraph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
            displayGraph.addEdge(edge.getTo(), edge.getFrom(), edge.getWeight());
        }

        return new CaseResult(line, displayGraph, result.getUsedEdges(), numberOfConnections);
    }

    public static class CaseResult {
        private final String outputLine;
        private final Graph graph;
        private final List<Edge> usedEdges;
        private final int totalCables;

        public CaseResult(String outputLine, Graph graph, List<Edge> usedEdges, int totalCables) {
            this.outputLine = outputLine;
            this.graph = graph;
            this.usedEdges = usedEdges;
            this.totalCables = totalCables;
        }

        public String getOutputLine() { return outputLine; }
        public Graph getGraph() { return graph; }
        public List<Edge> getUsedEdges() { return usedEdges; }
        public int getTotalCables() { return totalCables; }
    }
}