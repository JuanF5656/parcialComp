package algoritmos;

import estructuras.Position;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BFS {

    public static final int UNREACHABLE = -1;

    public static class Result {
        private final int distance;
        private final List<Position> path;

        public Result(int distance, List<Position> path) {
            this.distance = distance;
            this.path = path;
        }

        public int getDistance() { return distance; }
        public List<Position> getPath() { return path; }
    }

    private static final int[] DELTA_ROW = {-1, 1, 0, 0};
    private static final int[] DELTA_COL = {0, 0, -1, 1};

    public Result run(boolean[][] bomb, int rows, int cols, Position start, Position destination) {

        int startIndex = start.getRow() * cols + start.getCol();
        int destinationIndex = destination.getRow() * cols + destination.getCol();

        int totalCells = rows * cols;
        boolean[] visited = new boolean[totalCells];
        int[] parent = new int[totalCells];
        int[] distance = new int[totalCells];
        java.util.Arrays.fill(parent, -1);

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        visited[startIndex] = true;
        distance[startIndex] = 0;
        queue.add(startIndex);

        while (!queue.isEmpty()) {
            int currentIndex = queue.poll();

            if (currentIndex == destinationIndex) {
                return new Result(distance[currentIndex], buildPath(parent, cols, currentIndex));
            }

            int currentRow = currentIndex / cols;
            int currentCol = currentIndex % cols;

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = currentRow + DELTA_ROW[direction];
                int nextCol = currentCol + DELTA_COL[direction];

                if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) continue;

                int nextIndex = nextRow * cols + nextCol;
                if (visited[nextIndex] || bomb[nextRow][nextCol]) continue;

                visited[nextIndex] = true;
                parent[nextIndex] = currentIndex;
                distance[nextIndex] = distance[currentIndex] + 1;
                queue.add(nextIndex);
            }
        }

        return new Result(UNREACHABLE, Collections.emptyList());
    }

    private List<Position> buildPath(int[] parent, int cols, int destinationIndex) {
        List<Position> path = new ArrayList<>();
        int current = destinationIndex;
        while (current != -1) {
            path.add(new Position(current / cols, current % cols));
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }
}