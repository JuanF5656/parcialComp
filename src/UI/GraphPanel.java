package UI;

import estructuras.Edge;
import estructuras.Graph;

import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphPanel extends JPanel {

    private Graph graph;
    private final Map<Integer, Point> positions;
    private final Set<Long> highlightedEdges = new HashSet<>();
    private final Set<Integer> highlightedNodes = new HashSet<>();
    private boolean directed = false;

    public GraphPanel() {
        setBackground(Color.WHITE);
        positions = new HashMap<>();
    }

    public void setGraph(Graph graph) {
        setGraph(graph, null);
    }

    public void setGraph(Graph graph, List<Integer> path) {
        this.directed = false;
        this.graph = graph;
        createPositions();
        buildHighlight(path);
        repaint();
    }

    public void setGraphWithEdgeHighlights(Graph graph, List<Edge> highlightedEdgeList) {
        this.directed = false;
        this.graph = graph;
        createPositions();

        highlightedEdges.clear();
        highlightedNodes.clear();

        if (highlightedEdgeList != null) {
            for (Edge edge : highlightedEdgeList) {
                highlightedEdges.add(edgeKey(edge.getFrom(), edge.getTo()));
                highlightedNodes.add(edge.getFrom());
                highlightedNodes.add(edge.getTo());
            }
        }

        repaint();
    }

    /**
     * Mission 3's graph is directed (A -> B does not imply B -> A can be
     * walked), so both edges of a pair like 1->2 and 2->1 must be tracked
     * and highlighted independently — unlike {@link #setGraphWithEdgeHighlights},
     * whose edgeKey is intentionally symmetric for Missions 2 and 4.
     */
    public void setDirectedGraphWithEdgeHighlights(Graph graph, List<Edge> highlightedEdgeList) {
        this.directed = true;
        this.graph = graph;
        createPositions();

        highlightedEdges.clear();
        highlightedNodes.clear();

        if (highlightedEdgeList != null) {
            for (Edge edge : highlightedEdgeList) {
                highlightedEdges.add(directedEdgeKey(edge.getFrom(), edge.getTo()));
                highlightedNodes.add(edge.getFrom());
                highlightedNodes.add(edge.getTo());
            }
        }

        repaint();
    }

    private void buildHighlight(List<Integer> path) {
        highlightedEdges.clear();
        highlightedNodes.clear();
        if (path == null) return;
        highlightedNodes.addAll(path);
        for (int i = 0; i + 1 < path.size(); i++) {
            highlightedEdges.add(edgeKey(path.get(i), path.get(i + 1)));
        }
    }

    private long edgeKey(int a, int b) {
        int lo = Math.min(a, b), hi = Math.max(a, b);
        return ((long) lo << 32) | hi;
    }

    private long directedEdgeKey(int from, int to) {
        return ((long) from << 32) | (to & 0xffffffffL);
    }

    private void createPositions() {
        positions.clear();
        if (graph == null) return;
        int n = graph.getNumberOfVertices();
        int centerX = 500, centerY = 330, radius = 200;
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            positions.put(i, new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (graph == null) return;
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawEdges(g2);
        drawVertices(g2);
    }

    private void drawEdges(Graphics2D g2) {
        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            for (Edge edge : graph.getAdjacencyList().get(i)) {
                int from = edge.getFrom(), to = edge.getTo();
                Point fromPoint = positions.get(from), toPoint = positions.get(to);

                boolean isHighlighted = directed
                        ? highlightedEdges.contains(directedEdgeKey(from, to))
                        : highlightedEdges.contains(edgeKey(from, to));
                g2.setColor(isHighlighted ? new Color(0, 150, 80) : Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(isHighlighted ? 4 : 2));

                g2.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
                if (directed) {
                    drawArrowHead(g2, fromPoint, toPoint);
                }
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(String.valueOf(edge.getWeight()),
                        (fromPoint.x + toPoint.x) / 2, (fromPoint.y + toPoint.y) / 2);
            }
        }
    }

    private void drawArrowHead(Graphics2D g2, Point from, Point to) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        int nodeRadius = 25;
        int tipX = (int) (to.x - nodeRadius * Math.cos(angle));
        int tipY = (int) (to.y - nodeRadius * Math.sin(angle));
        int size = 9;
        int x1 = (int) (tipX - size * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (tipY - size * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (tipX - size * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (tipY - size * Math.sin(angle + Math.PI / 6));
        g2.fillPolygon(new int[]{tipX, x1, x2}, new int[]{tipY, y1, y2}, 3);
    }

    private void drawVertices(Graphics2D g2) {
        int r = 25;
        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            Point point = positions.get(i);
            boolean onPath = highlightedNodes.contains(i);

            g2.setColor(onPath ? new Color(0, 150, 80) : Color.WHITE);
            g2.fillOval(point.x - r, point.y - r, r * 2, r * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(point.x - r, point.y - r, r * 2, r * 2);
            g2.drawString(String.valueOf(i), point.x - 4, point.y + 5);
        }
    }

    private static class Point {
        final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }
}