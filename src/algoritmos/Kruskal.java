package algoritmos;

import estructuras.Edge;
import estructuras.UnionFind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {
    /*
     * Time complexity: O(C log C), where C is the number of cables.
     * Space complexity: O(N + C).
     */
    public KruskalResult minimumSpanningTree(int numberOfVertices, List<Edge> edges) {

        // Copy before sorting so callers can still use their original list afterward
        // (Mision4 reuses `edges` to build the drawing graph once this returns).
        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort(Comparator.comparingLong(Edge::getWeight));

        UnionFind unionFind = new UnionFind(numberOfVertices);

        long totalCost = 0;
        int edgesUsed = 0;
        List<Edge> usedEdges = new ArrayList<>();

        for (Edge edge : sorted) {
            int from = edge.getFrom();
            int to = edge.getTo();

            // connected(from, to) is also what silently rejects self-loops:
            // find(a) == find(a) is always true, so they never get added.
            if (!unionFind.connected(from, to)) {
                unionFind.union(from, to);
                totalCost += edge.getWeight();
                usedEdges.add(edge);
                edgesUsed++;

                if (edgesUsed == numberOfVertices - 1) break;
            }
        }

        if (edgesUsed != numberOfVertices - 1) {
            return new KruskalResult(-1, List.of());
        }

        return new KruskalResult(totalCost, usedEdges);
    }

    public static class KruskalResult {
        private final long totalCost;
        private final List<Edge> usedEdges;

        public KruskalResult(long totalCost, List<Edge> usedEdges) {
            this.totalCost = totalCost;
            this.usedEdges = usedEdges;
        }

        public long getTotalCost() { return totalCost; }
        public List<Edge> getUsedEdges() { return usedEdges; }
        public boolean isConnected() { return totalCost != -1; }
    }
}
