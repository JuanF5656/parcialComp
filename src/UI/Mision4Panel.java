package UI;

import estructuras.Graph;
import misiones.Mision4;
import parser.InputParser;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Mision4Panel extends JPanel {

    private static final int MAX_NODES_TO_DRAW = 100;
    private static final int MAX_CABLES_TO_DRAW = 300;

    private static final String SAMPLE_INPUT =
            "1\n4\n5\n1 2 10\n2 3 20\n3 4 30\n4 1 40\n1 3 15\n";

    private final JTextArea inputArea = new JTextArea(12, 30);
    private final JTextArea outputArea = new JTextArea(12, 30);
    private final JComboBox<String> caseSelector = new JComboBox<>();
    private final JLabel statusLabel = new JLabel(" ");
    private final GraphPanel graphPanel = new GraphPanel();

    private final Mision4 mision4 = new Mision4();
    private final List<Mision4.CaseResult> results = new ArrayList<>();

    public Mision4Panel() {
        setLayout(new BorderLayout(8, 8));
        outputArea.setEditable(false);

        JButton loadSample = new JButton("Load Sample");
        JButton run = new JButton("Run");
        loadSample.addActionListener(e -> inputArea.setText(SAMPLE_INPUT));
        run.addActionListener(e -> runMission());
        caseSelector.addActionListener(e -> showSelectedCase());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(loadSample);
        controls.add(run);
        controls.add(new JLabel("  Viewing:"));
        controls.add(caseSelector);

        JPanel leftSide = new JPanel(new GridLayout(2, 1, 4, 4));
        leftSide.add(new JScrollPane(inputArea));
        leftSide.add(new JScrollPane(outputArea));

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.add(statusLabel, BorderLayout.NORTH);
        rightSide.add(graphPanel, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, rightSide);
        split.setResizeWeight(0.4);

        add(controls, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void runMission() {
        results.clear();
        caseSelector.removeAllItems();
        graphPanel.setGraph(null);
        statusLabel.setText(" ");

        try {
            InputParser parser = new InputParser(
                    new ByteArrayInputStream(inputArea.getText().getBytes(StandardCharsets.UTF_8)));

            int testCases = parser.nextInt();
            StringBuilder output = new StringBuilder();

            for (int caseNumber = 1; caseNumber <= testCases; caseNumber++) {
                Mision4.CaseResult result = mision4.resolverCaso(parser, caseNumber);
                results.add(result);
                output.append(result.getOutputLine()).append("\n");
                caseSelector.addItem("Case #" + caseNumber);
            }

            outputArea.setText(output.toString());
            if (!results.isEmpty()) caseSelector.setSelectedIndex(0);

        } catch (IOException | NumberFormatException | NullPointerException ex) {
            outputArea.setText("Malformed input — check the format and try again.\n(" + ex.getMessage() + ")");
        }
    }

    private void showSelectedCase() {
        int index = caseSelector.getSelectedIndex();
        if (index < 0 || index >= results.size()) return;

        Mision4.CaseResult result = results.get(index);
        Graph graph = result.getGraph();
        int nodes = graph.getNumberOfVertices();
        int cables = result.getTotalCables();

        if (nodes > MAX_NODES_TO_DRAW || cables > MAX_CABLES_TO_DRAW) {
            graphPanel.setGraph(null);
            statusLabel.setText("Drawing omitted (" + nodes + " intersections, " + cables
                    + " cables). " + result.getOutputLine());
        } else {
            statusLabel.setText(result.getOutputLine());
            graphPanel.setGraphWithEdgeHighlights(graph, result.getUsedEdges());
        }
    }
}