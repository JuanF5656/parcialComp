package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class InputParser {

    private final BufferedReader reader;
    private StringTokenizer tokenizer;

    public InputParser(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public String next() throws IOException {

        while (tokenizer == null || !tokenizer.hasMoreTokens()) {

            String line = reader.readLine();

            if (line == null) {
                return null;
            }

            tokenizer = new StringTokenizer(line);
        }

        return tokenizer.nextToken();
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
        return Long.parseLong(next());
    }
}