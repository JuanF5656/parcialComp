package algoritmos;

import estructuras.Edge;
import estructuras.UnionFind;

import java.util.Comparator;
import java.util.List;

public class Kruskal {
    /*
     * Time complexity: O(C log C), where C is the number of cables.
     * Sorting the cables is the dominant operation.
     *
     * Space complexity: O(N + C), considering the list of cables
     * and the Union-Find structure.
     *
     * Kruskal is appropriate because it builds a minimum spanning
     * tree by selecting the cheapest cable that does not create a cycle.
     * Union-Find efficiently detects whether two intersections are
     * already connected.
     */
    public long minimumSpanningTree(int numberOfVertices, List<Edge> edges) {

        // Sort cables from cheapest to most expensive
        edges.sort(Comparator.comparingLong(Edge::getWeight));

        UnionFind unionFind = new UnionFind(numberOfVertices);

        long totalCost = 0;
        int edgesUsed = 0;

        for (Edge edge : edges) {

            int from = edge.getFrom();
            int to = edge.getTo();

            // Only use the cable if it does not create a cycle
            if (!unionFind.connected(from, to)) {

                unionFind.union(from, to);

                totalCost += edge.getWeight();
                edgesUsed++;

                // An MST with N vertices always has N - 1 edges
                if (edgesUsed == numberOfVertices - 1) {
                    break;
                }
            }
        }

        // If we didn't connect all vertices, an MST doesn't exist
        if (edgesUsed != numberOfVertices - 1) {
            return -1;
        }

        return totalCost;
    }
}
