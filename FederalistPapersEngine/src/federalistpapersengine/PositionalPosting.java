/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package federalistpapersengine;

import java.util.ArrayList;

/**
 *
 * @author Timothy
 */
public class PositionalPosting {
    private int docID;
    private int termFreq;
    private ArrayList<Integer> positions = new ArrayList<>();
    
    /**
     * Creates a new PositionalPosting object with the document id and list of
     * positions
     * @param docID document id containing the term
     * @param termFreq term frequency in the document
     * @param positions list of positions the term can be found
     */
    public PositionalPosting(int docID, int termFreq, 
            ArrayList<Integer> positions) {
        this.docID = docID;
        this.termFreq = termFreq;
        this.positions = positions;
    }
    
    public PositionalPosting(int docID, int termFreq) {
        this.docID = docID;
        this.termFreq = termFreq;
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

    /**
     * @return the termFreq
     */
    public int getTermFreq() {
        return termFreq;
    }

    /**
     * @param termFreq the termFreq to set
     */
    public void setTermFreq(int termFreq) {
        this.termFreq = termFreq;
    }
    
    public boolean isPositionsEmpty() {
        return positions.isEmpty();
    }
}
