package homework5;

import java.io.*;
import java.util.*;

/**
 * Reads tokens one at a time from an input stream. Returns tokens with minimal
 * processing: removing all non-alphanumeric characters, and converting to
 * lowercase.
 */
public class SimpleTokenStream implements TokenStream {

    private Scanner mReader;

    /**
     * Constructs a SimpleTokenStream to read from the specified file.
     */
    public SimpleTokenStream(File fileToOpen) throws FileNotFoundException {
        mReader = new Scanner(new FileReader(fileToOpen));
    }

    /**
     * Constructs a SimpleTokenStream to read from a String of text.
     */
    public SimpleTokenStream(String text) {
        mReader = new Scanner(text);
    }

    /**
     * Returns true if the stream has tokens remaining.
     */
    @Override
    public boolean hasNextToken() {
        return mReader.hasNext();
    }

    /**
     * Returns an array of tokens from the stream, or null if there is no token
     * available.
     */
    @Override
    public String nextToken() {
        //ArrayList<String> tokens = new ArrayList<>();
        if (!hasNextToken()) {
            return null;
        }

        // Convert the token to lowercase
        String next = mReader.next().toLowerCase();
        // Remove non-alphanumeric characters from the beginning and end and
        // apostrophes in the middle of the token
        next = next.replaceAll("^[^a-z0-9]+|[']+|[^a-z0-9]+$", "");

        return next.length() > 0 ? next
                : hasNextToken() ? nextToken() : null;
    }
}