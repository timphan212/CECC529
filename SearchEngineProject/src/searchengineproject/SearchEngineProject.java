/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Timothy
 */
public class SearchEngineProject {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // ask the user for the directory to index
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a directory to index: ");
        String dir = scan.next();
        
        // concat the directory to the end of the main directory
        final Path currentWorkingPath = Paths.get(Paths.get("").toAbsolutePath()
                + "\\" + dir);
        System.out.println("Indexing " + dir);
        
        // index the directory and retrieve a list of file names
        List<String> fileNames = indexDirectory(currentWorkingPath);
        System.out.println("Successfully indexed " + fileNames.size()
                + " files.\n");
        System.out.print("Enter a search query: ");
        
        // retrieve the search query from user and parse
        String query = scan.next();
        parseQuery(query);
        
        scan.close();
        
    }
    
    /**
     * Goes through each file in the directory and indexes the file by adding
     * the terms to the positional inverted index
     * @param currentWorkingPath the directory of the corpus
     * @return
     * @throws IOException 
     */
    private static List<String> indexDirectory(Path currentWorkingPath) throws IOException {
        // the list of file names that were processed
        final List<String> fileNames = new ArrayList<String>();
        
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
                    // we have found a .txt file; add its name to the fileName list,
                    // then index the file and increase the document ID counter.
                    // System.out.println("Indexing file " + file.getFileName());

                    fileNames.add(file.getFileName().toString());
                    //indexFile(file.toFile(), index, mDocumentID);
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
        
        return fileNames;
    }
    
    /**
     * Indexes a file by reading a series of tokens from the file, treating each
     * token as a term, and then adding the given document's ID to the inverted
     * index for the term.
     *
     * @param file a File object for the document to index.
     * @param index the current state of the index for the files that have
     * already been processed.
     * @param docID the integer ID of the current document, needed when indexing
     * each term from the document.
     */
    private static void indexFile(File file, int docID) {
      // TO-DO: finish this method for indexing a particular file.
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.

        try {
            SimpleTokenStream tokeStream = new SimpleTokenStream(file);
            String term = tokeStream.nextToken();
            System.out.println(term);
            while (term != null) {
                term = PorterStemmer.processToken(term);
                //index.addTerm(term, docID);
                term = tokeStream.nextToken();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parses the search query to perform the specified operation
     * @param query a string representing the search query
     */
    private static void parseQuery(String query) {
        while(query.compareTo(":q") != 0) {
            
            
        }
    }
}
