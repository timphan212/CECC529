/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Tim
 */
public class BiwordIndex {
    private final HashMap<String, ArrayList<Integer>> mIndex;
    
    public BiwordIndex() {
        mIndex = new HashMap<>();
    }
    
    /** 
     * Add the term to the index
     * @param term term to be added to the index
     * @param documentID document the term was found in
     */
    public void addTerm(String term, int documentID) {
        // check if the term is already in the index
        if(!mIndex.containsKey(term)) {
            // create a new positional posting list
            ArrayList<Integer> docList = new ArrayList<>();
            // add the positional posting list to the list
            docList.add(documentID);
            // add to the index
            mIndex.put(term, docList);
        }
        // the term was found in the index
        else {
            // retrieve the list of positional postings
            ArrayList<Integer> docList = mIndex.get(term);
            // check if the last document in the list is not the same document
            if(docList.get(docList.size() - 1) != documentID) {
                // create a new PositionalPosting and add the positional post
                // to the current list
                docList.add(documentID);
                // replace the old list of positional postings with the new one
                mIndex.replace(term, docList);
            }
        }
    }
    
    /**
     * Return the file list of the term
     * @param term a string representing the term
     * @return 
     */
    public ArrayList<Integer> getFileList(String term) {
        return mIndex.get(term);
    }
    
    /**
     * Return the size of the index
     * @return an integer value representing the size of the index
     */
    public int getTermCount() {
        return mIndex.size();
    }
        
    /**
     * Gets the list of all the terms
     * @return a string array containing all the terms
     */
    public String[] getTerms() {
        String[] terms = mIndex.keySet().toArray(new String[mIndex.size()]);
        Arrays.sort(terms);
        
        return terms;
    }
}
