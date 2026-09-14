package UI;

import estructuras.Graph;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

    private final GraphPanel graphPanel;

    public MainWindow() {

        setTitle("The Feline Graph Chronicles");

        setSize(1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        graphPanel = new GraphPanel();

        add(graphPanel);

        setVisible(true);
    }

    public void setGraph(Graph graph) {

        graphPanel.setGraph(graph);
    }
}