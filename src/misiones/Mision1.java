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

    public List<String> resolverTodosLosCasos(InputParser parser) throws IOException {
        List<String> outputs = new ArrayList<>();
        int caseNumber = 1;

        while (true) {
            int rows = parser.nextInt();
            int cols = parser.nextInt();
            if (rows == 0 && cols == 0) break;

            outputs.add(resolverCaso(parser, rows, cols, caseNumber));
            caseNumber++;
        }
        return outputs;
    }

    private String resolverCaso(InputParser parser, int rows, int cols, int caseNumber) throws IOException {

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
            return "Case #" + caseNumber + ": Nina is unreachable";
        }

        if (start.equals(destination)) {
            return "Case #" + caseNumber + ": BFS 0 DFS 0";
        }

        BFS.Result bfsResult = bfs.run(bomb, rows, cols, start, destination);
        if (bfsResult.getDistance() == BFS.UNREACHABLE) {
            return "Case #" + caseNumber + ": Nina is unreachable";
        }

        DFS.Result dfsResult = dfs.run(bomb, rows, cols, start, destination);

        return "Case #" + caseNumber + ": BFS " + bfsResult.getDistance()
                + " DFS " + dfsResult.getDistance();
    }
}