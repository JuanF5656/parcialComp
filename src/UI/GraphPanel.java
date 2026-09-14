package UI;

import estructuras.Edge;
import estructuras.Graph;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

public class GraphPanel extends JPanel {

    private Graph graph;

    private final Map<Integer, Point> positions;

    public GraphPanel() {

        setBackground(Color.WHITE);

        positions = new HashMap<>();
    }

    public void setGraph(Graph graph) {

        this.graph = graph;

        createPositions();

        repaint();
    }

    private void createPositions() {

        positions.clear();

        if (graph == null) {
            return;
        }

        int numberOfVertices = graph.getNumberOfVertices();

        int centerX = 500;
        int centerY = 330;
        int radius = 200;

        for (int i = 0; i < numberOfVertices; i++) {

            double angle =
                    2 * Math.PI * i / numberOfVertices;

            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));

            positions.put(i, new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        if (graph == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) graphics;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawEdges(g2);
        drawVertices(g2);
    }

    private void drawEdges(Graphics2D g2) {

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));

        for (int i = 0; i < graph.getNumberOfVertices(); i++) {

            for (Edge edge : graph.getAdjacencyList().get(i)) {

                int from = edge.getFrom();
                int to = edge.getTo();

                Point fromPoint = positions.get(from);
                Point toPoint = positions.get(to);

                g2.drawLine(
                        fromPoint.x,
                        fromPoint.y,
                        toPoint.x,
                        toPoint.y
                );

                int middleX =
                        (fromPoint.x + toPoint.x) / 2;

                int middleY =
                        (fromPoint.y + toPoint.y) / 2;

                g2.drawString(
                        String.valueOf(edge.getWeight()),
                        middleX,
                        middleY
                );
            }
        }
    }

    private void drawVertices(Graphics2D g2) {

        int vertexRadius = 25;

        for (int i = 0; i < graph.getNumberOfVertices(); i++) {

            Point point = positions.get(i);

            int x = point.x - vertexRadius;
            int y = point.y - vertexRadius;

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    x,
                    y,
                    vertexRadius * 2,
                    vertexRadius * 2
            );

            g2.setColor(Color.BLACK);

            g2.drawOval(
                    x,
                    y,
                    vertexRadius * 2,
                    vertexRadius * 2
            );

            String label = String.valueOf(i + 1);

            g2.drawString(
                    label,
                    point.x - 4,
                    point.y + 5
            );
        }
    }

    private static class Point {

        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
