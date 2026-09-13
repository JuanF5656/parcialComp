package estructuras;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final int numberOfVertices;
    private final List<List<Edge>> adjacencyList;

    public Graph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;

        adjacencyList = new ArrayList<>();

        for (int i = 0; i < numberOfVertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public void addEdge(int from, int to, long weight) {
        adjacencyList.get(from).add(
                new Edge(from, to, weight)
        );
    }
}