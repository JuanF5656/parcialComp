package UI;

import estructuras.Graph;
import misiones.Mision2;
import parser.InputParser;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Mision2Panel extends JPanel {

    private static final int MAX_NODES_TO_DRAW = 60;

    private static final String SAMPLE_INPUT =
            "3\n2 1 0 1\n0 1 100\n3 3 2 0\n0 1 100\n0 2 200\n1 2 50\n2 0 0 1\n";

    private final JTextArea inputArea = new JTextArea(12, 30);
    private final JTextArea outputArea = new JTextArea(12, 30);
    private final JComboBox<String> caseSelector = new JComboBox<>();
    private final JLabel statusLabel = new JLabel(" ");
    private final GraphPanel graphPanel = new GraphPanel();

    private final Mision2 mision2 = new Mision2();
    private final List<Mision2.CaseResult> results = new ArrayList<>();

    public Mision2Panel() {
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
                Mision2.CaseResult result = mision2.resolverCaso(parser, caseNumber);
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

        Mision2.CaseResult result = results.get(index);
        Graph graph = result.getGraph();

        if (graph.getNumberOfVertices() > MAX_NODES_TO_DRAW) {
            graphPanel.setGraph(null);
            statusLabel.setText("Drawing omitted (" + graph.getNumberOfVertices()
                    + " nodes > " + MAX_NODES_TO_DRAW + "). " + result.getOutputLine());
        } else {
            statusLabel.setText(result.getOutputLine());
            graphPanel.setGraph(graph, result.getPath());
        }
    }
}
