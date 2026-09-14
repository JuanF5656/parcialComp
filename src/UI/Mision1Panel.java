package UI;

import misiones.Mision1;
import parser.InputParser;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Mision1Panel extends JPanel {

    private static final int MAX_GRID_SIDE_TO_DRAW = 50; // Section 2.3

    private static final String SAMPLE_INPUT =
            "10 10\n9\n0 1 2\n1 1 2\n2 2 2 9\n3 2 1 7\n5 3 3 6 9\n6 4 0 1 2 7\n"
                    + "7 3 0 3 8\n8 2 7 9\n9 3 2 3 4\n0 0\n9 9\n0 0\n";

    private final JTextArea inputArea = new JTextArea(12, 30);
    private final JTextArea outputArea = new JTextArea(12, 30);
    private final JComboBox<String> caseSelector = new JComboBox<>();
    private final ButtonGroup pathChoiceGroup = new ButtonGroup();
    private final JRadioButton showBfs = new JRadioButton("BFS path", true);
    private final JRadioButton showDfs = new JRadioButton("DFS path");
    private final JLabel statusLabel = new JLabel(" ");
    private final GridPanel gridPanel = new GridPanel();

    private final Mision1 mision1 = new Mision1();
    private List<Mision1.CaseResult> results = List.of();

    public Mision1Panel() {
        setLayout(new BorderLayout(8, 8));
        outputArea.setEditable(false);

        pathChoiceGroup.add(showBfs);
        pathChoiceGroup.add(showDfs);
        showBfs.addActionListener(e -> showSelectedCase());
        showDfs.addActionListener(e -> showSelectedCase());

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
        controls.add(new JLabel("  Show:"));
        controls.add(showBfs);
        controls.add(showDfs);

        JPanel leftSide = new JPanel(new GridLayout(2, 1, 4, 4));
        leftSide.add(new JScrollPane(inputArea));
        leftSide.add(new JScrollPane(outputArea));

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.add(statusLabel, BorderLayout.NORTH);
        rightSide.add(gridPanel, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, rightSide);
        split.setResizeWeight(0.4);

        add(controls, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void runMission() {
        results = List.of();
        caseSelector.removeAllItems();
        gridPanel.clear();
        statusLabel.setText(" ");

        try {
            InputParser parser = new InputParser(
                    new ByteArrayInputStream(inputArea.getText().getBytes(StandardCharsets.UTF_8)));

            results = mision1.resolverTodosLosCasos(parser);

            StringBuilder output = new StringBuilder();
            for (int i = 0; i < results.size(); i++) {
                output.append(results.get(i).getOutputLine()).append("\n");
                caseSelector.addItem("Case #" + (i + 1));
            }

            outputArea.setText(output.toString());
            if (!results.isEmpty()) caseSelector.setSelectedIndex(0);

        } catch (IOException | NumberFormatException | NullPointerException
                 | ArrayIndexOutOfBoundsException ex) {
            outputArea.setText("Malformed input — check the format and try again.\n(" + ex.getMessage() + ")");
        }
    }

    private void showSelectedCase() {
        int index = caseSelector.getSelectedIndex();
        if (index < 0 || index >= results.size()) return;

        Mision1.CaseResult result = results.get(index);
        int rows = result.getRows();
        int cols = result.getCols();

        if (rows > MAX_GRID_SIDE_TO_DRAW || cols > MAX_GRID_SIDE_TO_DRAW) {
            gridPanel.clear();
            statusLabel.setText("Drawing omitted (" + rows + "x" + cols
                    + " > " + MAX_GRID_SIDE_TO_DRAW + "x" + MAX_GRID_SIDE_TO_DRAW + "). " + result.getOutputLine());
        } else {
            statusLabel.setText(result.getOutputLine());
            List<estructuras.Position> path = showBfs.isSelected() ? result.getBfsPath() : result.getDfsPath();
            gridPanel.setGrid(rows, cols, result.getBomb(), result.getStart(), result.getDestination(), path);
        }
    }
}