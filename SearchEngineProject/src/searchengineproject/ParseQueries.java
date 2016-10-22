/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim
 */
public class ParseQueries {
        /**
     * Searches the index for the query and merge the results to display to the
     * user
     * @param query the query to search the index with
     */
    public static ArrayList<String> searchResults(String query, 
            BiwordIndex bindex, PositionalInvertedIndex index,
            List<String> fileNames) {
        String[] tokens = query.split(" ");
        ArrayList<Integer> files1 = new ArrayList<>();
        ArrayList<ArrayList<PositionalPosting>> phraseList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> fileList = new ArrayList<>();
        boolean orFlag = false, phraseFlag = false, biwordFlag = false;
        String prevWord = "";
        
        // print out the files for only one literal
        if(tokens.length == 1) {
            return printFiles(searchToken(tokens[0], index), fileNames);
        }
        // there is more than one literal and needs extra parsing
        else {
            // loop through each literal
            for(int i = 0; i < tokens.length; i++) {
                // check if the token starts with a quotation mark
                if(tokens[i].startsWith("\"")) {
                    // check if the token after ends with a quotation mark
                    // to use biword index instead
                    if(tokens[i+1].endsWith("\"")) {
                        biwordFlag = true;
                        prevWord = PorterStemmer.processToken(tokens[i]
                                .substring(1));
                    }
                    // the phrase length is not equal to two, have to add to
                    // a phrase list then use the positional inverted index
                    else {
                        phraseList.add(index.getPositionalPosting(PorterStemmer
                                .processToken(tokens[i].substring(1))));
                        phraseFlag = true;
                    }
                }
                // if the token ends with a quotation mark and the biword flag
                // is set then combine the two terms and check
                else if(tokens[i].endsWith("\"") && biwordFlag == true) {
                    biwordFlag = false;
                    // or flag is false then need to AND the phrase query with
                    //the previous token in the query
                    if(orFlag == false) {
                        files1 = MergeQueries
                                .mergeFileLists(files1, bindex.getFileList
                                    (prevWord + " " + PorterStemmer
                                .processToken(tokens[i].substring
                                    (0, tokens[i].length()-1))));
                    }
                    // or flag is true need to set files1 equal to the results
                    // from biword index to add to the orFileLists
                    else {
                        files1 = bindex.getFileList
                                (prevWord + " " + PorterStemmer
                                .processToken(tokens[i].substring
                                (0, tokens[i].length()-1)));
                        orFlag = false;
                    }
                }
                // check if the token ends with a quotation mark and the phrase
                // is larger than 2
                else if(tokens[i].endsWith("\"") && biwordFlag == false) {
                    phraseList.add(index.getPositionalPosting(PorterStemmer
                            .processToken(tokens[i].substring(0, tokens[i]
                            .length()-1))));
                    phraseFlag = false;
                    
                    if(orFlag == false) {
                        files1 = MergeQueries.mergeFileLists(files1, 
                                MergeQueries.phraseMergeFileLists(phraseList));
                    }
                    else {
                        files1 = MergeQueries.phraseMergeFileLists(phraseList);
                        orFlag = false;
                    }
                }
                // check if the phrase flag is true then add to the phrase list
                else if(phraseFlag == true) {
                    phraseList.add(index.getPositionalPosting(PorterStemmer
                                    .processToken(tokens[i])));
                }
                // get the file list for the first token or if the flag was set
                else if(i == 0 || orFlag == true) {
                    files1 = searchToken(tokens[i], index);
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
                    files1 = MergeQueries.mergeFileLists(files1,
                            searchToken(tokens[i], index));
                }
            }
            // add the last file list to the lists of files
            fileList.add(files1);
            // OR the queries together and print it out
            return printFiles(MergeQueries.orFileLists(fileList), fileNames);
        }
    }
    
    /**
     * Searches the index for the token and then returns a list of integers
     * of which files contain the token
     * @param token a word
     * @return return a list of integers representing the files the token is
     * located at
     */
    private static ArrayList<Integer> searchToken(String token, 
            PositionalInvertedIndex index) {
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
     * Prints out the document name and the number of documents found
     * @param files a list of integers representing the files
     */
    private static ArrayList<String> printFiles(ArrayList<Integer> files,
            List<String> fileNames) {
        ArrayList<String> fileResults = new ArrayList<>();

        if(files != null) {
            // loop through the list of files
            for(Integer file : files) {
                //System.out.println(fileNames.get(file));
                fileResults.add(fileNames.get(file));
            }
        }

        //System.out.println(files.size() + " documents found.");
        return fileResults;
    }
}
