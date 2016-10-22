/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Timothy
 */
public class DirectoryIndex {
    private List<String> fileNames = new ArrayList<>();
    private PositionalInvertedIndex index = new 
        PositionalInvertedIndex();
    private BiwordIndex bindex = new BiwordIndex();
    
    public PositionalInvertedIndex getPositionalInvertedIndex() {
        return index;
    }
    
    public List<String> getFileNames() {
        return fileNames;
    }
    
    public BiwordIndex getBiwordIndex() {
        return bindex;
    }
    
    /**
     * Goes through each file in the directory and indexes the file by adding
     * the terms to the positional inverted index
     * @param currentWorkingPath the directory of the corpus
     * @return
     * @throws IOException 
     */
    public void indexDirectory(Path currentWorkingPath) 
            throws IOException {
        // create a new list of file names and new index
        fileNames = new ArrayList<String>();
        index = new PositionalInvertedIndex();
        // This is our standard "walk through all .txt files" code.
        Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {
            int mDocumentID = 0;

            public FileVisitResult preVisitDirectory(Path dir,
                    BasicFileAttributes attrs) {
                // make sure we only process the current working directory
                if (currentWorkingPath.equals(dir)) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

            public FileVisitResult visitFile(Path file,
                    BasicFileAttributes attrs) {
                // only process .txt files
                if (file.toString().endsWith(".json")) {
                    // we have found a .json file; add its name to the fileName
                    // list, then index the file and increase the document ID
                    // counter.
                    System.out.println(file.getFileName().toString());
                    fileNames.add(file.getFileName().toString());
                    indexFile(file.toFile(), mDocumentID);
                    mDocumentID++;
                }
                return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            public FileVisitResult visitFileFailed(Path file,
                    IOException e) {

                return FileVisitResult.CONTINUE;
            }

        });
    }
    
    /**
     * Indexes a file by reading a series of tokens from the file, treating each
     * token as a term, and then adding the given document's ID to the inverted
     * index for the term.
     *
     * @param file a File object for the document to index.
     * @param docID the integer ID of the current document, needed when indexing
     * each term from the document.
     */
    private void indexFile(File file, int docID) {
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.

        try {
            // use GSON to grab the body tag from the document
            Gson gson = new Gson();
            // open the file with gson
            NPSDocument npsDoc = gson.fromJson(new FileReader(file),
                    NPSDocument.class);
            // pass the body string to the token stream
            SimpleTokenStream tokeStream = new SimpleTokenStream(npsDoc.body);
            // get the next token in the stream
            String term = tokeStream.nextToken();
            // counter to keep track of position
            int counter = 0;
            // string to keep track of the previous word for biword
            String prevWord = "";
            
            // loop until the term returned is null
            while(term != null) {
                // skip the term if the word is empty
                if(term.compareTo("") != 0) {
                    // save the first word in the file as the previous word
                    if(counter == 0) {
                        prevWord = term;
                    }
                    // stem the previous token and the current token then add to
                    // the biword index and set the previous word to the current
                    // word
                    else {
                        bindex.addTerm(PorterStemmer.processToken(prevWord)
                            + " " + PorterStemmer
                            .processToken(term), docID);
                        prevWord = term;
                    }
                    // if the term contains a hyphen then remove the hyphen and
                    // stem then index the token, then split the string by
                    // the hyphen, stem and add each word individually
                    if(term.contains("-")) {
                        index.addTerm(PorterStemmer.processToken(term
                                .replace("-", "")), docID, counter);
                        String[] hyphenWord = term.split("-");
                        
                        for(String word : hyphenWord) {
                            index.addTerm(PorterStemmer.processToken(word),
                                docID, counter);
                        }
                    }
                    // stem and add the token to the positional inverted index
                    else {
                        index.addTerm(PorterStemmer.processToken(term),
                            docID, counter);
                    }
                }
                // get the next token and increment the position counter
                term = tokeStream.nextToken();
                counter++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class NPSDocument {
	String title;
	String body;
	String url;
}