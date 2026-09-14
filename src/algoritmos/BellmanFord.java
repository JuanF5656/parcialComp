package algoritmos;

import estructuras.Edge;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class BellmanFord {

    public static final long NO_ROUTE = Long.MIN_VALUE;

    public static class Result {
        private final long[] maxChurunFromSource;
        private final boolean[] unbounded;

        public Result(long[] maxChurunFromSource, boolean[] unbounded) {
            this.maxChurunFromSource = maxChurunFromSource;
            this.unbounded = unbounded;
        }

        public long[] getMaxChurunFromSource() { return maxChurunFromSource; }
        public boolean[] getUnbounded() { return unbounded; }
    }

    public Result run(int n, List<Edge> edges, int source) {
        long[] dist = new long[n];
        java.util.Arrays.fill(dist, NO_ROUTE);
        dist[source] = 0;

        for (int round = 0; round < n - 1; round++) {
            boolean improvedAny = false;
            for (Edge edge : edges) {
                if (dist[edge.getFrom()] == NO_ROUTE) continue;
                long candidate = dist[edge.getFrom()] + edge.getWeight();
                if (candidate > dist[edge.getTo()]) {
                    dist[edge.getTo()] = candidate;
                    improvedAny = true;
                }
            }
            if (!improvedAny) break;
        }

        boolean[] seedOfCycle = new boolean[n];
        for (Edge edge : edges) {
            if (dist[edge.getFrom()] == NO_ROUTE) continue;
            long candidate = dist[edge.getFrom()] + edge.getWeight();
            if (candidate > dist[edge.getTo()]) seedOfCycle[edge.getTo()] = true;
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
        return new Result(dist, unbounded);
    }
}