package misiones;

import algoritmos.BFS;
import algoritmos.DFS;
import estructuras.Position;
import parser.InputParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mision1 {

    private final BFS bfs;
    private final DFS dfs;

    public Mision1() {
        bfs = new BFS();
        dfs = new DFS();
    }

    /**
     * Mission 1 has no upfront "number of test cases" line (unlike the
     * other missions): it is terminated by a sentinel test case with
     * R = 0 and C = 0, which must NOT be processed. That is why this loops
     * internally and returns every case at once, instead of a
     * resolverCaso(parser, caseNumber) driven by an external for-loop like
     * Mision2/Mision4.
     */
    public List<CaseResult> resolverTodosLosCasos(InputParser parser) throws IOException {

        List<CaseResult> results = new ArrayList<>();
        int caseNumber = 1;

        while (true) {
            int rows = parser.nextInt();
            int cols = parser.nextInt();
            if (rows == 0 && cols == 0) {
                break;
            }
            results.add(resolverCaso(parser, rows, cols, caseNumber));
            caseNumber++;
        }

        return results;
    }

    private CaseResult resolverCaso(InputParser parser, int rows, int cols, int caseNumber) throws IOException {

        boolean[][] bomb = new boolean[rows][cols];
        int bombRowCount = parser.nextInt();

        for (int i = 0; i < bombRowCount; i++) {
            int rowNumber = parser.nextInt();
            int bombsInRow = parser.nextInt();
            for (int j = 0; j < bombsInRow; j++) {
                bomb[rowNumber][parser.nextInt()] = true;
            }
        }

        Position start = new Position(parser.nextInt(), parser.nextInt());
        Position destination = new Position(parser.nextInt(), parser.nextInt());

        if (bomb[start.getRow()][start.getCol()] || bomb[destination.getRow()][destination.getCol()]) {
            String line = "Case #" + caseNumber + ": Nina is unreachable";
            return new CaseResult(line, rows, cols, bomb, start, destination, List.of(), List.of());
        }

        if (start.equals(destination)) {
            String line = "Case #" + caseNumber + ": BFS 0 DFS 0";
            List<Position> trivial = List.of(start);
            return new CaseResult(line, rows, cols, bomb, start, destination, trivial, trivial);
        }

        BFS.Result bfsResult = bfs.run(bomb, rows, cols, start, destination);

        if (bfsResult.getDistance() == BFS.UNREACHABLE) {
            String line = "Case #" + caseNumber + ": Nina is unreachable";
            return new CaseResult(line, rows, cols, bomb, start, destination, List.of(), List.of());
        }

        DFS.Result dfsResult = dfs.run(bomb, rows, cols, start, destination);

        String line = "Case #" + caseNumber + ": BFS " + bfsResult.getDistance()
                + " DFS " + dfsResult.getDistance();

        return new CaseResult(line, rows, cols, bomb, start, destination, bfsResult.getPath(), dfsResult.getPath());
    }

    public static class CaseResult {
        private final String outputLine;
        private final int rows;
        private final int cols;
        private final boolean[][] bomb;
        private final Position start;
        private final Position destination;
        private final List<Position> bfsPath;
        private final List<Position> dfsPath;

        public CaseResult(String outputLine, int rows, int cols, boolean[][] bomb, Position start,
                          Position destination, List<Position> bfsPath, List<Position> dfsPath) {
            this.outputLine = outputLine;
            this.rows = rows;
            this.cols = cols;
            this.bomb = bomb;
            this.start = start;
            this.destination = destination;
            this.bfsPath = bfsPath;
            this.dfsPath = dfsPath;
        }

        public String getOutputLine() { return outputLine; }
        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public boolean[][] getBomb() { return bomb; }
        public Position getStart() { return start; }
        public Position getDestination() { return destination; }
        public List<Position> getBfsPath() { return bfsPath; }
        public List<Position> getDfsPath() { return dfsPath; }
    }
}