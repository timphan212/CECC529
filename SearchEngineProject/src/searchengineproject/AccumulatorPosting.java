/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

/**
 *
 * @author Timothy
 */
public class AccumulatorPosting {
    private int docID;
    private double accumulator;

    public AccumulatorPosting(int docID, double accumulator) {
        this.docID = docID;
        this.accumulator = accumulator;
    }

    public int getDocID() {
        return docID;
    }

    public double getAccumulator() {
        return accumulator;
    }
}
