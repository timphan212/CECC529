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
    private List<String> fileNames = new ArrayList<>();
    private PositionalInvertedIndex index = new 
        PositionalInvertedIndex();
    
    public PositionalInvertedIndex getPositionalInvertedIndex() {
        return index;
    }
    
    public List<String> getFileNames() {
        return fileNames;
    }
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) throws IOException {
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
        
    }*/
    
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
            NPSDocument npsDoc = gson.fromJson(new FileReader(file),
                    NPSDocument.class);
            // pass the body string to the token stream
            SimpleTokenStream tokeStream = new SimpleTokenStream(npsDoc.body);
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
    /*private void parseQuery(String query) throws IOException {
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
                    String formatQuery = query.toLowerCase();
                    formatQuery = formatQuery.replaceAll("[-]+|[\']", "");
                    searchResults(formatQuery);
                    break;
            }
            
            System.out.print("\nEnter a search query: ");
            query = scan.nextLine();
            queryTokens = query.split(" ");
        }
        
        scan.close();
    }*/
    
    /**
     * Print out all the terms in the vocabulary of the corpus
     */
    /*public void printVocab() {
        // Get the list of terms in the corpus
        String[] terms = index.getTerms();
        
        // Loop through each term to print them out
        for(String term : terms) {
            System.out.println(term);
        }
        
        // Print out the count of the total number
        System.out.println(index.getTermCount());
    }*/
    
    /**
     * Searches the index for the query and merge the results to display to the
     * user
     * @param query the query to search the index with
     */
    public ArrayList<String> searchResults(String query) {
        String[] tokens = query.split(" ");
        ArrayList<Integer> files1 = new ArrayList<>();
        ArrayList<ArrayList<PositionalPosting>> phraseList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> fileList = new ArrayList<>();
        boolean orFlag = false, phraseFlag = false;
        
        // print out the files for only one literal
        if(tokens.length == 1) {
            return printFiles(searchToken(tokens[0]));
        }
        // there is more than one literal and needs extra parsing
        else {
            // loop through each literal
            for(int i = 0; i < tokens.length; i++) {
                // check if the token starts with a quotation mark
                if(tokens[i].startsWith("\"")) {
                    phraseList.add(index.getPositionalPosting(PorterStemmer
                            .processToken(tokens[i].substring(1))));
                    phraseFlag = true;
                }
                // check if the token ends with a quotation mark
                else if(tokens[i].endsWith("\"")) {
                    phraseList.add(index.getPositionalPosting(PorterStemmer
                            .processToken(tokens[i].substring(0, tokens[i]
                                    .length()-1))));
                    phraseFlag = false;
                    files1 = mergeFileLists(files1, 
                            phraseMergeFileLists(phraseList));
                }
                // check if the phrase flag is true then add to the phrase list
                else if(phraseFlag == true) {
                    phraseList.add(index.getPositionalPosting(PorterStemmer
                                    .processToken(tokens[i])));
                }
                // get the file list for the first token or if the flag was set
                else if(i == 0 || orFlag == true) {
                    files1 = searchToken(tokens[i]);
                    orFlag = false;
                }
                // the current token is a "+", add the current merged lists to
                // the file lists
                else if(tokens[i].compareTo("+") == 0) {
                    fileList.add(files1);
                    orFlag = true;
                }
                // merge the files (AND the queries)
                else {
                    files1 = mergeFileLists(files1, searchToken(tokens[i]));
                }
            }
            // add the last file list to the lists of files
            fileList.add(files1);
            // OR the queries together and print it out
            return printFiles(orFileLists(fileList));
        }
    }
    
    /**
     * Searches the index for the token and then returns a list of integers
     * of which files contain the token
     * @param token a word
     * @return return a list of integers representing the files the token is
     * located at
     */
    private ArrayList<Integer> searchToken(String token) {
        ArrayList<Integer> files = new ArrayList<>();
        token = token.replaceAll("[\"]", "");
        // stem the token and then get the list of postional postings for the
        // token
        ArrayList<PositionalPosting> pospostList = index
                .getPositionalPosting(PorterStemmer.processToken(token));
        
        // if the token exists then add document id to a list
        if(pospostList != null) {
            for(PositionalPosting pospost : pospostList) {
                files.add(pospost.getDocID());
            }
        }
        
        return files;
    }
    
    /**
     * Finds the intersection of two file lists
     * @param fileList1 the first list of files
     * @param fileList2 the second list of files
     * @return a merged list of fileList1 and fileList2
     */
    private ArrayList<Integer> mergeFileLists
        (ArrayList<Integer> fileList1, ArrayList<Integer> fileList2) {
        ArrayList<Integer> mergedFiles = new ArrayList<>();
        int i = 0, j = 0;
        
        if(fileList1.isEmpty()) {
            return fileList2;
        }
        if(fileList2.isEmpty()) {
            return fileList1;
        }
        // keep looping until one of the list reaches the end
        while(i < fileList1.size() && j < fileList2.size()) {
            // if the document id for the first list is less than the second
            // then increment the pointer for the first list
            if(fileList1.get(i) < fileList2.get(j)) {
                i++;
            }
            // if the document id for the first list is greater than the second
            // then increment the pointer for the second list
            else if(fileList1.get(i) > fileList2.get(j)) {
                j++;
            }
            // else if the document ids match then add the file number to the
            // list and increment both pointers
            else {
                mergedFiles.add(fileList1.get(i));
                i++;
                j++;    
            }
        }
        
        return mergedFiles;    
    }
    
    /**
     * Finds the list of files where the phrase query can be found
     * @param fileList list of lists of positional postings for each token
     * @return return a list of integers representing the files
     */
    private ArrayList<Integer> phraseMergeFileLists(
            ArrayList<ArrayList<PositionalPosting>> fileList) {
        // array list containing the files where the phrase is
        ArrayList<Integer> phraseFiles = new ArrayList<>();
        // the first token's list of positional posting
        ArrayList<PositionalPosting> file = fileList.get(0);

        if(file != null) {
            // Loop through the list of positional posting for the first token
            for(PositionalPosting posPost : file) {
                // grab the document id of the positional posting
                int docID = posPost.getDocID();
                // grab the list of positions associated with the document
                ArrayList<Integer> positions = posPost.getPositions();
                // boolean to check if the phrase was found
                boolean phraseFound = false;
                // counter to check if the phrase found matches the number of 
                // tokens
                int counter = 1;

                // loop through the other token's list of positional postings
                for(int j = 1; j < fileList.size(); j++) {
                    // loop through the positional postings of the other tokens
                    if(fileList.get(j) != null) {
                        for(PositionalPosting otherPosPost : fileList.get(j)) {
                            // check if the document ids match the first token
                            if(otherPosPost.getDocID() == docID) {
                                // loop through the positions of the first token
                                // for one of the positional posting
                                for(Integer position : positions) {
                                    // check if the other token's positions
                                    // contains the position of the first 
                                    // token + 1
                                    if(otherPosPost.getPositions()
                                            .contains(position + j)) {
                                        // set the boolean to be true
                                        phraseFound = true;
                                        // increment the counter
                                        counter++;
                                        break;
                                    }
                                }
                            }
                            // break out of the loop if the document id
                            // surpasses the first token's document id
                            else if(otherPosPost.getDocID() > docID) {
                                break;
                            }
                        }
                        // break out of the loop to prevent searching through 
                        // the next token's positional posting if it could not
                        // find the current token
                        if(phraseFound == false) {
                            break;
                        }
                    }
                }
                // if the phrase boolean is true and the counter is equal to the
                // length of the phrase query then add the file to the list
                if(phraseFound == true && fileList.size() == counter) {
                    phraseFiles.add(docID);
                }
            }
        }
        
        return phraseFiles;
    }
    
    /**
     * Loops through the list of lists to merge them into one list
     * @param fileList a list of lists of files
     * @return a list of integers that is from merging all the lists
     */
    private ArrayList<Integer> orFileLists(
            ArrayList<ArrayList<Integer>> fileList) {
        ArrayList<Integer> orFiles = new ArrayList<>();
        
        // if the file list is only size of 1 then just set orFiles equal to it
        if(fileList.size() == 1) {
            orFiles = fileList.get(0);
        }
        // else loop through all the lists, remove duplicates, then add them
        else {
            for(ArrayList<Integer> files : fileList) {
                orFiles.removeAll(files);
                orFiles.addAll(files);
            }
        }
        
        return orFiles;
    }
    
    /**
     * Prints out the document name and the number of documents found
     * @param files a list of integers representing the files
     */
    private ArrayList<String> printFiles(ArrayList<Integer> files) {
        ArrayList<String> fileResults = new ArrayList<>();
        
        // loop through the list of files
        for(Integer file : files) {
            //System.out.println(fileNames.get(file));
            fileResults.add(fileNames.get(file));
        }
        
        //System.out.println(files.size() + " documents found.");
        return fileResults;
    }
}

class NPSDocument {
	String title;
	String body;
	String url;
}