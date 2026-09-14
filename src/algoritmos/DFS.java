package algoritmos;

import estructuras.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DFS {

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
        int[] nextDirection = new int[totalCells];
        int[] depth = new int[totalCells];
        java.util.Arrays.fill(parent, -1);

        int[] stack = new int[totalCells];
        int stackTop = 0;

        visited[startIndex] = true;
        depth[startIndex] = 0;
        stack[stackTop++] = startIndex;

        while (stackTop > 0) {
            int currentIndex = stack[stackTop - 1];

            if (currentIndex == destinationIndex) {
                return new Result(depth[currentIndex], buildPath(parent, cols, currentIndex));
            }

            int currentRow = currentIndex / cols;
            int currentCol = currentIndex % cols;
            boolean advanced = false;

            while (nextDirection[currentIndex] < 4) {
                int direction = nextDirection[currentIndex]++;
                int nextRow = currentRow + DELTA_ROW[direction];
                int nextCol = currentCol + DELTA_COL[direction];

                if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) continue;

                int nextIndex = nextRow * cols + nextCol;
                if (visited[nextIndex] || bomb[nextRow][nextCol]) continue;

                visited[nextIndex] = true;
                parent[nextIndex] = currentIndex;
                depth[nextIndex] = depth[currentIndex] + 1;
                stack[stackTop++] = nextIndex;
                advanced = true;
                break;
            }

            if (!advanced) {
                stackTop--; // backtrack
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