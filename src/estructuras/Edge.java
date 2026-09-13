package estructuras;

public class Edge {

    private final int from;
    private final int to;
    private final long weight;

    public Edge(int from, int to, long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public long getWeight() {
        return weight;
    }
}
