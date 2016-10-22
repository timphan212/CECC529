/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public class MergeQueries {
    /**
     * Loops through the list of lists to merge them into one list
     * @param fileList a list of lists of files
     * @return a list of integers that is from merging all the lists
     */
    public static ArrayList<Integer> orFileLists(
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
     * Finds the intersection of two file lists
     * @param fileList1 the first list of files
     * @param fileList2 the second list of files
     * @return a merged list of fileList1 and fileList2
     */
    public static ArrayList<Integer> mergeFileLists
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
    public static ArrayList<Integer> phraseMergeFileLists(
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
}
