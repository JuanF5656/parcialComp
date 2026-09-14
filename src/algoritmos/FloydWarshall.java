package algoritmos;

import estructuras.Edge;
import java.util.List;

public class FloydWarshall {

    public static final long NO_ROUTE = Long.MIN_VALUE;

    public static class Result {
        private final long[][] maxChurun;
        private final boolean[][] unbounded;

        public Result(long[][] maxChurun, boolean[][] unbounded) {
            this.maxChurun = maxChurun;
            this.unbounded = unbounded;
        }

        public long[][] getMaxChurun() { return maxChurun; }
        public boolean[][] getUnbounded() { return unbounded; }
    }

    public Result run(int n, List<Edge> edges) {
        long[][] dist = new long[n][n];
        for (long[] row : dist) java.util.Arrays.fill(row, NO_ROUTE);
        for (int i = 0; i < n; i++) dist[i][i] = 0;

        for (Edge edge : edges) {
            int a = edge.getFrom(), b = edge.getTo();
            long w = edge.getWeight();
            if (dist[a][b] == NO_ROUTE || w > dist[a][b]) dist[a][b] = w;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == NO_ROUTE) continue;
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] == NO_ROUTE) continue;
                    long candidate = dist[i][k] + dist[k][j];
                    if (dist[i][j] == NO_ROUTE || candidate > dist[i][j]) {
                        dist[i][j] = candidate;
                    }
                }
            }
        }

        boolean[][] unbounded = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (dist[i][j] == NO_ROUTE) continue;
                for (int k = 0; k < n; k++) {
                    if (dist[i][k] != NO_ROUTE && dist[k][k] > 0 && dist[k][j] != NO_ROUTE) {
                        unbounded[i][j] = true;
                        break;
                    }
                }
            }
        }
        return new Result(dist, unbounded);
    }
}