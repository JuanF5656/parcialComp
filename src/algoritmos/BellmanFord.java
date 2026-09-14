package algoritmos;

import estructuras.Edge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BellmanFord {

    public static final long NO_ROUTE = Long.MIN_VALUE;

    public Result run(int n, List<Edge> edges, int source) {
        long[] dist = new long[n];
        Edge[] predecessorEdge = new Edge[n]; // edge used to reach each node with the current best value
        java.util.Arrays.fill(dist, NO_ROUTE);
        dist[source] = 0;

        for (int round = 0; round < n - 1; round++) {
            boolean improvedAny = false;
            for (Edge edge : edges) {
                if (dist[edge.getFrom()] == NO_ROUTE) continue;
                long candidate = dist[edge.getFrom()] + edge.getWeight();
                if (candidate > dist[edge.getTo()]) {
                    dist[edge.getTo()] = candidate;
                    predecessorEdge[edge.getTo()] = edge;
                    improvedAny = true;
                }
            }
            if (!improvedAny) break;
        }

        // Extra round: any node that still improves belongs to, or is fed
        // by, a positive-gain cycle.
        boolean[] seedOfCycle = new boolean[n];
        for (Edge edge : edges) {
            if (dist[edge.getFrom()] == NO_ROUTE) continue;
            long candidate = dist[edge.getFrom()] + edge.getWeight();
            if (candidate > dist[edge.getTo()]) {
                seedOfCycle[edge.getTo()] = true;
                predecessorEdge[edge.getTo()] = edge; // so cycle extraction walks the real cycle
            }
        }

        List<List<Integer>> adjacency = new ArrayList<>();
        for (int i = 0; i < n; i++) adjacency.add(new ArrayList<>());
        for (Edge edge : edges) adjacency.get(edge.getFrom()).add(edge.getTo());

        boolean[] unbounded = new boolean[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (seedOfCycle[i] && !unbounded[i]) {
                unbounded[i] = true;
                queue.add(i);
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adjacency.get(u)) {
                if (!unbounded[v]) {
                    unbounded[v] = true;
                    queue.add(v);
                }
            }
        }

        return new Result(dist, unbounded, predecessorEdge, n);
    }

    public static class Result {
        private final long[] maxChurunFromSource;
        private final boolean[] unbounded;
        private final Edge[] predecessorEdge;
        private final int n;

        public Result(long[] maxChurunFromSource, boolean[] unbounded, Edge[] predecessorEdge, int n) {
            this.maxChurunFromSource = maxChurunFromSource;
            this.unbounded = unbounded;
            this.predecessorEdge = predecessorEdge;
            this.n = n;
        }

        public long[] getMaxChurunFromSource() { return maxChurunFromSource; }
        public boolean[] getUnbounded() { return unbounded; }

        /**
         * Reconstructs the walk from source to target as a list of edges,
         * by following predecessor edges backward. Only meaningful when
         * target is reachable and not unbounded.
         */
        public List<Edge> reconstructPath(int source, int target) {
            List<Edge> path = new ArrayList<>();
            int current = target;
            while (current != source) {
                Edge e = predecessorEdge[current];
                if (e == null) {
                    return path; // best-effort; should not happen if reachable
                }
                path.add(e);
                current = e.getFrom();
            }
            Collections.reverse(path);
            return path;
        }

        /**
         * Extracts one positive-gain cycle responsible for a given
         * unbounded node: walk predecessors n steps first (guaranteeing we
         * land inside the cycle, not just fed into it), then follow
         * predecessors until a node repeats.
         */
        public List<Edge> extractResponsibleCycle(int unboundedNode, int n) {
            int current = unboundedNode;
            for (int i = 0; i < n; i++) {
                Edge e = predecessorEdge[current];
                if (e == null) {
                    return List.of();
                }
                current = e.getFrom();
            }
            int cycleStart = current;
            List<Edge> cycle = new ArrayList<>();
            do {
                Edge e = predecessorEdge[current];
                if (e == null) {
                    return List.of();
                }
                cycle.add(e);
                current = e.getFrom();
            } while (current != cycleStart);
            Collections.reverse(cycle);
            return cycle;
        }
    }
}