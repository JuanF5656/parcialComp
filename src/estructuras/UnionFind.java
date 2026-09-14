package estructuras;

public class UnionFind {

    private int[] parent;
    private int[] size;

    public UnionFind(int numberOfElements) {

        parent = new int[numberOfElements];
        size = new int[numberOfElements];

        for (int i = 0; i < numberOfElements; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int element) {

        if (parent[element] != element) {
            parent[element] = find(parent[element]);
        }

        return parent[element];
    }

    public void union(int first, int second) {

        int rootFirst = find(first);
        int rootSecond = find(second);

        if (rootFirst == rootSecond) {
            return;
        }

        if (size[rootFirst] < size[rootSecond]) {
            parent[rootFirst] = rootSecond;
            size[rootSecond] += size[rootFirst];

        } else {
            parent[rootSecond] = rootFirst;
            size[rootFirst] += size[rootSecond];
        }
    }

    public boolean connected(int first, int second) {
        return find(first) == find(second);
    }
}
