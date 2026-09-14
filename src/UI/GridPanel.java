package UI;

import estructuras.Position;

import javax.swing.JPanel;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GridPanel extends JPanel {

    private boolean[][] bomb;
    private int rows;
    private int cols;
    private Position start;
    private Position destination;
    private Set<Position> pathCells = new HashSet<>();

    public GridPanel() {
        setBackground(Color.WHITE);
    }

    public void setGrid(int rows, int cols, boolean[][] bomb, Position start, Position destination,
                        List<Position> path) {
        this.rows = rows;
        this.cols = cols;
        this.bomb = bomb;
        this.start = start;
        this.destination = destination;
        this.pathCells = new HashSet<>(path == null ? List.of() : path);
        repaint();
    }

    public void clear() {
        this.bomb = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (bomb == null || rows <= 0 || cols <= 0) return;

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 10;
        int cellSize = Math.max(2, Math.min(
                (getWidth() - 2 * margin) / cols,
                (getHeight() - 2 * margin) / rows));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = margin + c * cellSize;
                int y = margin + r * cellSize;
                Position p = new Position(r, c);

                Color fill;
                if (bomb[r][c]) {
                    fill = new Color(200, 60, 60);
                } else if (p.equals(start)) {
                    fill = new Color(60, 140, 220);
                } else if (p.equals(destination)) {
                    fill = new Color(230, 90, 130);
                } else if (pathCells.contains(p)) {
                    fill = new Color(0, 150, 80); // same green used for highlighted edges/nodes elsewhere
                } else {
                    fill = new Color(245, 245, 245);
                }

                g2.setColor(fill);
                g2.fillRect(x, y, cellSize, cellSize);

                if (cellSize > 3) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }
}