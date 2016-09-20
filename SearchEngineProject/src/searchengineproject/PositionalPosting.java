/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import java.util.ArrayList;

/**
 *
 * @author Timothy
 */
public class PositionalPosting {
    private int docID;
    private ArrayList<Integer> positions = new ArrayList<>();
    
    /**
     * Creates a new PositionalPosting object with the document id and list of
     * positions
     * @param docID document id containing the term
     * @param positions list of positions the term can be found
     */
    public PositionalPosting(int docID, ArrayList<Integer> positions) {
        this.docID = docID;
        this.positions = positions;
    }
    
    /**
     * Get the document id for the posting
     * @return 
     */
    public int getDocID() {
        return this.docID;
    }
    
    /**
     * Set the document id for the posting
     * @param docID the document id to replace the old document id
     */
    public void setDocID(int docID) {
        this.docID = docID;
    }
    
    /**
     * Get the list of positions for the term is found in the document
     * @return a list of positions
     */
    public ArrayList<Integer> getPositions() {
        return this.positions;
    }
    
    /**
     * Replace the old positions list with a new position list
     * @param positions list of positions
     */
    public void setPositions(ArrayList<Integer> positions) {
        this.positions = positions;
    }
}
