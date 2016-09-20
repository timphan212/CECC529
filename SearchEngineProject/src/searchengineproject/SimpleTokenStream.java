package searchengineproject;

import java.io.*;
import java.util.*;

/**
Reads tokens one at a time from an input stream. Returns tokens with minimal
processing: removing all non-alphanumeric characters, and converting to 
lowercase.
*/
public class SimpleTokenStream implements TokenStream {
   private Scanner mReader;

   /**
   Constructs a SimpleTokenStream to read from the specified file.
   */
   public SimpleTokenStream(File fileToOpen) throws FileNotFoundException {
      mReader = new Scanner(new FileReader(fileToOpen));
   }
   
   /**
   Constructs a SimpleTokenStream to read from a String of text.
   */
   public SimpleTokenStream(String text) {
      mReader = new Scanner(text);
   }

   /**
   Returns true if the stream has tokens remaining.
   */
   @Override
   public boolean hasNextToken() {
      return mReader.hasNext();
   }

   /**
   Returns an array of tokens from the stream, or null if there is no token
   available.
   */
    public ArrayList<String> nextTokens() {
        ArrayList<String> tokens = new ArrayList<>();
        if (!hasNextToken()) {
            return null;
        }
        
        // Convert the token to lowercase
        String next = mReader.next().toLowerCase();
        // Remove non-alphanumeric characters from the beginning and end and
        // apostrophes in the middle of the token
        next = next.replaceAll("^[^a-z0-9]+|[']+|[^a-z0-9]+$", "");
        
        // check if the token has a hyphen
        if(next.contains("-")) {
            // add the word without the hyphen
            tokens.add(next.replace("-", ""));
            // split the word on the hyphen then add each individual word to 
            // the list
            tokens.addAll(Arrays.asList(next.split("-")));
        }
        // token does not contain a hyphen therefore just add to list
        else {
            tokens.add(next);
        }
        
        return tokens.size() > 0 ? tokens
                : hasNextToken() ? nextTokens() : null;
    }

    @Override
    public String nextToken() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}