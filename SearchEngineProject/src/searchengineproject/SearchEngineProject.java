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
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Timothy
 */
public class SearchEngineProject {
    private static List<String> fileNames = new ArrayList<>();
    private static PositionalInvertedIndex index = new 
        PositionalInvertedIndex();
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
        indexDirectory(currentWorkingPath);
        System.out.println("Successfully indexed " + fileNames.size()
                + " files.\n");
        System.out.print("Enter a search query: ");
        
        // retrieve the search query from user and parse
        scan.nextLine();
        String query = scan.nextLine();
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
    private static void indexDirectory(Path currentWorkingPath) 
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
    private static void indexFile(File file, int docID) {
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.

        try {
            SimpleTokenStream tokeStream = new SimpleTokenStream(file);
            // Get the list of terms
            ArrayList<String> terms = tokeStream.nextTokens();
            int counter = 0;
            
            while(terms != null) {
                // Loop through the list of terms
                for(String term : terms) {
                    // Stem the term
                    term = PorterStemmer.processToken(term);
                    // Add the term to the index
                    index.addTerm(term, docID, counter);
                }
                
                // Get the next list of terms
                terms = tokeStream.nextTokens();
                // Increment the position counter
                counter++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parses the search query to perform the specified operation
     * @param query a string representing the search query
     */
    private static void parseQuery(String query) throws IOException {
        String[] queryTokens = query.split(" ");
        Scanner scan = new Scanner(System.in);
        
        while(queryTokens[0].compareTo(":q") != 0) {
            switch(queryTokens[0]) {
                case ":stem":
                    // porter stem term
                    System.out.println(PorterStemmer
                            .processToken(queryTokens[1]));
                    break;
                case ":index":
                    // index the specified directory
                    final Path currentWorkingPath = Paths.get(Paths.get("")
                            .toAbsolutePath() + "\\" + queryTokens[1]);
                    indexDirectory(currentWorkingPath);
                    System.out.println("Successfully indexed " + 
                            fileNames.size() + " files.");
                    break;
                case ":vocab":
                    // print all terms in the vocabulary of the corpus
                    printVocab();
                    break;
                default:
                    //do the search query
                    searchResults(query);
                    break;
            }
            
            System.out.print("\nEnter a search query: ");
            query = scan.nextLine();
            queryTokens = query.split(" ");
        }
        
        scan.close();
    }
    
    /**
     * Print out all the terms in the vocabulary of the corpus
     */
    private static void printVocab() {
        // Get the list of terms in the corpus
        String[] terms = index.getTerms();
        
        // Loop through each term to print them out
        for(String term : terms) {
            System.out.println(term);
        }
        
        // Print out the count of the total number
        System.out.println(index.getTermCount());
    }
    
    /**
     * Searches the index for the query and merge the results to display to the
     * user
     * @param query the query to search the index with
     */
    private static void searchResults(String query) {
        String[] tokens = query.split(" ");
        boolean flag = false;
        String phrase = "";
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> files = new ArrayList<>();
        int counter = 0;
        
        // use some kind of leading pointer to pass two at a time to merge
        for(int i = 0; i < tokens.length; i++) {
            // phrase query look up first term then check if position of second term is +1
            // or query merge results
            // and query find intersection
        }
    }
    
    private static ArrayList<Integer> searchToken(String token) {
        ArrayList<Integer> files = new ArrayList<>();
        
        ArrayList<PositionalPosting> pospostList = index
                .getPositionalPosting(PorterStemmer.processToken(token));
        for(PositionalPosting pospost : pospostList) {
            files.add(pospost.getDocID());
        }
        
        return files;
    }
    
    private static ArrayList<Integer> mergeFileLists
        (ArrayList<Integer> fileList1, ArrayList<Integer> fileList2) {
        ArrayList<Integer> mergedFiles = new ArrayList<>();
        int i = 0, j = 0;
        
        while(i < fileList1.size() || j < fileList2.size()) {
            if(fileList1.get(i) < fileList2.get(j)) {
                i++;
            }
            else if(fileList1.get(i) > fileList2.get(j)) {
                j++;
            }
            else {
                mergedFiles.add(fileList1.get(i));
                i++;
                j++;    
            }
        }
        
        return mergedFiles;    
    }
}