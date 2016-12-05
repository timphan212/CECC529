/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package federalistpapersengine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Tim
 */
public class JayDocuments {
    private ArrayList<Integer> files;
    private ArrayList<Double> wdtVector;
    private double centroid;
    private HashMap<Integer, ArrayList<Double>> docVec;
    
    public JayDocuments(ArrayList<Integer> files) {
        this.files = files;
    }
    
    public ArrayList<Integer> getFiles() {
        return this.files;
    }
       
    public double getCentroid() {
        return this.centroid;
    }
    
    public void setCentroid(double centroid) {
        this.centroid = centroid;
    }
    
    public void getDocumentVector(DiskInvertedIndex dindex) {
        ArrayList<String> terms = dindex.getPositionalIndexTerms();
        
        for(String term : terms) {
            ArrayList<PositionalPosting> posPostList = dindex.GetPostings(term, true);
            
            for(PositionalPosting posPost : posPostList) {
                for(Integer file : files) {
                    if(file == posPost.getDocID()) {
                        
                    }
                }
            }
        }
    }
}
