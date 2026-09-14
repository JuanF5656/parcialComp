package algoritmos;

import estructuras.Edge;
import estructuras.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

    public static final long INF = Long.MAX_VALUE;

    public DijkstraResult shortestPath(Graph graph, int start, int destination) {

        int n = graph.getNumberOfVertices();
        long[] distance = new long[n];
        int[] predecessor = new int[n];
        Arrays.fill(distance, INF);
        Arrays.fill(predecessor, -1);
        distance[start] = 0;

        PriorityQueue<NodeDistance> priorityQueue =
                new PriorityQueue<>((a, b) -> Long.compare(a.distance, b.distance));
        priorityQueue.add(new NodeDistance(start, 0));

        while (!priorityQueue.isEmpty()) {
            NodeDistance current = priorityQueue.poll();
            if (current.distance != distance[current.node]) continue;
            if (current.node == destination) break; // finalized, safe to stop

            for (Edge edge : graph.getAdjacencyList().get(current.node)) {
                int neighbor = edge.getTo();
                long newDistance = current.distance + edge.getWeight();

                if (newDistance < distance[neighbor]) {
                    distance[neighbor] = newDistance;
                    predecessor[neighbor] = current.node;
                    priorityQueue.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }

        if (distance[destination] == INF) {
            return new DijkstraResult(INF, List.of());
        }

        List<Integer> path = new ArrayList<>();
        for (int at = destination; at != -1; at = predecessor[at]) {
            path.add(at);
        }
        java.util.Collections.reverse(path);

        return new DijkstraResult(distance[destination], path);
    }

    public static class DijkstraResult {
        private final long distance;
        private final List<Integer> path;

        public DijkstraResult(long distance, List<Integer> path) {
            this.distance = distance;
            this.path = path;
        }

        public long getDistance() { return distance; }
        public List<Integer> getPath() { return path; }
        public boolean isReachable() { return distance != INF; }
    }

    private static class NodeDistance {
        private final int node;
        private final long distance;
        public NodeDistance(int node, long distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}