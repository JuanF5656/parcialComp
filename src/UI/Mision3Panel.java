package UI;

import algoritmos.FloydWarshall;
import misiones.Mision3;
import parser.InputParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Mision3Panel extends JPanel {

    private static final int MAX_NODES_TO_DRAW = 60;  // Section 2.3: Missions 2 and 3 -> up to 60 nodes
    private static final int MAX_MATRIX_N = 100;       // Section 2.3: matrix shown for every N up to 100

    private static final String SAMPLE_INPUT =
            "3\n5 7 0 4\n0 1 50\n0 2 10\n1 2 -30\n1 3 40\n2 1 -5\n2 3 60\n3 4 20\n"
                    + "4 4 0 3\n0 1 20\n1 2 30\n2 1 -10\n2 3 15\n"
                    + "3 3 0 2\n0 1 -40\n1 2 -25\n0 2 -80\n";

    private final JTextArea inputArea = new JTextArea(12, 30);
    private final JTextArea outputArea = new JTextArea(12, 30);
    private final JComboBox<String> caseSelector = new JComboBox<>();
    private final JLabel statusLabel = new JLabel(" ");
    private final GraphPanel graphPanel = new GraphPanel();
    private final JTable matrixTable = new JTable();

    private final Mision3 mision3 = new Mision3();
    private final java.util.ArrayList<Mision3.CaseResult> results = new java.util.ArrayList<>();

    public Mision3Panel() {
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

        JTabbedPane drawingTabs = new JTabbedPane();
        drawingTabs.addTab("Graph", graphPanel);
        drawingTabs.addTab("N x N matrix", new JScrollPane(matrixTable));

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.add(statusLabel, BorderLayout.NORTH);
        rightSide.add(drawingTabs, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, rightSide);
        split.setResizeWeight(0.4);

        add(controls, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void runMission() {
        results.clear();
        caseSelector.removeAllItems();
        graphPanel.setGraph(null);
        matrixTable.setModel(new DefaultTableModel());
        statusLabel.setText(" ");

        try {
            InputParser parser = new InputParser(
                    new ByteArrayInputStream(inputArea.getText().getBytes(StandardCharsets.UTF_8)));

            int testCases = parser.nextInt();
            StringBuilder output = new StringBuilder();

            for (int caseNumber = 1; caseNumber <= testCases; caseNumber++) {
                Mision3.CaseResult result = mision3.resolverCaso(parser, caseNumber);
                results.add(result);
                output.append(result.getOutputLine()).append("\n");
                caseSelector.addItem("Case #" + caseNumber);
            }

            outputArea.setText(output.toString());
            if (!results.isEmpty()) caseSelector.setSelectedIndex(0);

        } catch (IllegalStateException ex) {
            // Exactly the Floyd-Warshall / Bellman-Ford cross-check mismatch
            // required by Section 5, item 2.
            outputArea.setText("Cross-check mismatch: " + ex.getMessage());
        } catch (IOException | NumberFormatException | NullPointerException
                 | ArrayIndexOutOfBoundsException ex) {
            outputArea.setText("Malformed input — check the format and try again.\n(" + ex.getMessage() + ")");
        }
    }

    private void showSelectedCase() {
        int index = caseSelector.getSelectedIndex();
        if (index < 0 || index >= results.size()) return;

        Mision3.CaseResult result = results.get(index);
        int n = result.getN();

        if (n > MAX_NODES_TO_DRAW) {
            graphPanel.setGraph(null);
            statusLabel.setText("Drawing omitted (" + n + " nodes > " + MAX_NODES_TO_DRAW + "). "
                    + result.getOutputLine());
        } else {
            statusLabel.setText(result.getOutputLine());
            graphPanel.setDirectedGraphWithEdgeHighlights(result.getGraph(), result.getHighlighted());
        }

        fillMatrix(result);
    }

    private void fillMatrix(Mision3.CaseResult result) {
        int n = result.getN();

        if (n > MAX_MATRIX_N) {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Matrix omitted: N = " + n + " is above the limit of " + MAX_MATRIX_N);
            matrixTable.setModel(model);
            return;
        }

        long[][] matrix = result.getMatrix();
        boolean[][] unbounded = result.getMatrixUnbounded();

        String[] columnNames = new String[n + 1];
        columnNames[0] = "";
        for (int j = 0; j < n; j++) columnNames[j + 1] = String.valueOf(j);

        Object[][] rows = new Object[n][n + 1];
        for (int i = 0; i < n; i++) {
            rows[i][0] = String.valueOf(i);
            for (int j = 0; j < n; j++) {
                long value = matrix[i][j];
                if (value == FloydWarshall.NO_ROUTE) {
                    rows[i][j + 1] = "-";
                } else if (unbounded[i][j]) {
                    rows[i][j + 1] = "inf";
                } else {
                    rows[i][j + 1] = String.valueOf(value);
                }
            }
        }

        matrixTable.setModel(new DefaultTableModel(rows, columnNames));
    }
}