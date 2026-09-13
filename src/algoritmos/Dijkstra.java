package algoritmos;

import estructuras.Edge;
import estructuras.Graph;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijkstra {

    private static final long INF = Long.MAX_VALUE;

    public long shortestPath(Graph graph, int start, int destination) {

        int numberOfVertices = graph.getNumberOfVertices();

        // distance[i] = cheapest known cost to reach node i
        long[] distance = new long[numberOfVertices];
        Arrays.fill(distance, INF);

        // The cost of reaching the starting node is 0
        distance[start] = 0;

        // PriorityQueue stores nodes ordered by their current distance
        PriorityQueue<NodeDistance> priorityQueue =
                new PriorityQueue<>(
                        (a, b) -> Long.compare(a.distance, b.distance)
                );

        priorityQueue.add(new NodeDistance(start, 0));

        while (!priorityQueue.isEmpty()) {

            NodeDistance current = priorityQueue.poll();

            int currentNode = current.node;
            long currentDistance = current.distance;

            // Ignore outdated entries
            if (currentDistance != distance[currentNode]) {
                continue;
            }

            // We reached the destination
            if (currentNode == destination) {
                return currentDistance;
            }

            // Examine all edges connected to the current node
            for (Edge edge : graph.getAdjacencyList().get(currentNode)) {

                int neighbor = edge.getTo();
                long weight = edge.getWeight();

                long newDistance = currentDistance + weight;

                // Found a cheaper route
                if (newDistance < distance[neighbor]) {

                    distance[neighbor] = newDistance;

                    priorityQueue.add(
                            new NodeDistance(neighbor, newDistance)
                    );
                }
            }
        }

        // Destination cannot be reached
        return INF;
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
