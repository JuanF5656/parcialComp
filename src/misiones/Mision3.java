package misiones;

import algoritmos.BellmanFord;
import algoritmos.FloydWarshall;
import estructuras.Edge;
import estructuras.Graph;
import parser.InputParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mision3 {

    public enum Outcome { BLOCKED, UNBOUNDED, FINITE }

    private final FloydWarshall floydWarshall;
    private final BellmanFord bellmanFord;

    public Mision3() {
        floydWarshall = new FloydWarshall();
        bellmanFord = new BellmanFord();
    }

    public CaseResult resolverCaso(InputParser parser, int caseNumber) throws IOException {

        int n = parser.nextInt();
        int m = parser.nextInt();
        int source = parser.nextInt();
        int destination = parser.nextInt();

        List<Edge> edges = new ArrayList<>();
        Graph graph = new Graph(n);

        for (int i = 0; i < m; i++) {
            Edge edge = new Edge(parser.nextInt(), parser.nextInt(), parser.nextLong());
            edges.add(edge);
            graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight()); // directed: added once only
        }

        FloydWarshall.Result fw = floydWarshall.run(n, edges);
        BellmanFord.Result bf = bellmanFord.run(n, edges, source);

        long fwValue = fw.getMaxChurun()[source][destination];
        boolean fwUnbounded = fw.getUnbounded()[source][destination];

        // Cross-check: Bellman-Ford's result for D must agree with the
        // Floyd-Warshall matrix (Section 5, requirement 2). A mismatch here
        // signals a bug in one of the two implementations, so we fail loudly
        // instead of silently printing a wrong answer; the GUI surfaces this
        // same check to the user (Section 5, "the GUI must report a mismatch
        // if the two ever disagree").
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

        String line;
        Outcome outcome;
        List<Edge> highlighted;

        if (fwValue == FloydWarshall.NO_ROUTE) {
            line = "Case #" + caseNumber + ": Limon blocked the way";
            outcome = Outcome.BLOCKED;
            highlighted = List.of();
        } else if (fwUnbounded) {
            line = "Case #" + caseNumber + ": Infinite churun!";
            outcome = Outcome.UNBOUNDED;
            highlighted = bf.extractResponsibleCycle(destination, n);
        } else {
            line = "Case #" + caseNumber + ": " + fwValue;
            outcome = Outcome.FINITE;
            highlighted = bf.reconstructPath(source, destination);
        }

        return new CaseResult(line, n, graph, edges, fw.getMaxChurun(), fw.getUnbounded(),
                outcome, highlighted, source, destination);
    }

    public static class CaseResult {
        private final String outputLine;
        private final int n;
        private final Graph graph;
        private final List<Edge> edges;
        private final long[][] matrix;
        private final boolean[][] matrixUnbounded;
        private final Outcome outcome;
        private final List<Edge> highlighted;
        private final int source;
        private final int destination;

        public CaseResult(String outputLine, int n, Graph graph, List<Edge> edges, long[][] matrix,
                          boolean[][] matrixUnbounded, Outcome outcome, List<Edge> highlighted,
                          int source, int destination) {
            this.outputLine = outputLine;
            this.n = n;
            this.graph = graph;
            this.edges = edges;
            this.matrix = matrix;
            this.matrixUnbounded = matrixUnbounded;
            this.outcome = outcome;
            this.highlighted = highlighted;
            this.source = source;
            this.destination = destination;
        }

        public String getOutputLine() { return outputLine; }
        public int getN() { return n; }
        public Graph getGraph() { return graph; }
        public List<Edge> getEdges() { return edges; }
        public long[][] getMatrix() { return matrix; }
        public boolean[][] getMatrixUnbounded() { return matrixUnbounded; }
        public Outcome getOutcome() { return outcome; }
        public List<Edge> getHighlighted() { return highlighted; }
        public int getSource() { return source; }
        public int getDestination() { return destination; }
    }
}