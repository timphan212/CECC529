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
 * @author Timothy
 */
public class PositionalInvertedIndex {
    private final HashMap<String, ArrayList<PositionalPosting>> mIndex;
    
    public PositionalInvertedIndex() {
        mIndex = new HashMap<>();
    }
    /** 
     * Add the term to the index
     * @param term term to be added to the index
     * @param documentID document the term was found in
     * @param position the position of the term in the document
     */
    public void addTerm(String term, int documentID, int position) {
        // check if the term is already in the index
        if(!mIndex.containsKey(term)) {
            // create a new positional posting list
            ArrayList<PositionalPosting> pospostList = new ArrayList<>();
            // add the positional posting list to the list
            pospostList.add(createPositionalPosting(documentID, position));
            // add to the index
            mIndex.put(term, pospostList);
        }
        // the term was found in the index
        else {
            // retrieve the list of positional postings
            ArrayList<PositionalPosting> pospostList = mIndex.get(term);
            // check if the last document in the list is not the same document
            if(pospostList.get(pospostList.size() - 1).getDocID() 
                    != documentID) {
                // create a new PositionalPosting and add the positional post
                // to the current list
                pospostList.add(createPositionalPosting(documentID, position));
                // replace the old list of positional postings with the new one
                mIndex.replace(term, pospostList);
            }
            // the last document is the same as the current document
            else {
                PositionalPosting pospost = pospostList.get(pospostList.size() - 1);
                ArrayList<Integer> positions = pospost.getPositions();
                positions.add(position);
                pospost.setPositions(positions);
                pospostList.set(pospostList.size() - 1, pospost);
                mIndex.replace(term, pospostList);
            }
        }
    }
    
    /**
     * Creates a new PositionalPosting object
     * @param docID the document id to be added to the list
     * @param position the position of the term
     * @return 
     */
    private PositionalPosting createPositionalPosting(int docID, int position) {
        // create a new position list
        ArrayList<Integer> posList = new ArrayList<>();
        // add the position to the list
        posList.add(position);
        // create a new positional posting for the new document
        PositionalPosting pospost = new PositionalPosting(docID, posList.size(),
                posList);
        
        return pospost;
    }
    
    public ArrayList<PositionalPosting> getPositionalPosting(String term) {
        return mIndex.get(term);
    }
    
    public int getTermCount() {
        return mIndex.size();
    }
    
    
    public String[] getTerms() {
        String[] terms = mIndex.keySet().toArray(new String[mIndex.size()]);
        Arrays.sort(terms);
        
        return terms;
    }
}
