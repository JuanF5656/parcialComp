import estructuras.Graph;
import UI.MainWindow;

public class Main {

    public static void main(String[] args) {

        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 0, 10);

        graph.addEdge(1, 2, 20);
        graph.addEdge(2, 1, 20);

        graph.addEdge(2, 3, 30);
        graph.addEdge(3, 2, 30);

        graph.addEdge(3, 0, 40);
        graph.addEdge(0, 3, 40);

        graph.addEdge(0, 2, 15);
        graph.addEdge(2, 0, 15);

        MainWindow window = new MainWindow();

        window.setGraph(graph);
    }
}