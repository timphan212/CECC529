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
    private double[] centroid;
    private HashMap<Integer, double[]> docVec;
    
    public JayDocuments(ArrayList<Integer> files) {
        this.files = files;
    }
    
    public ArrayList<Integer> getFiles() {
        return this.files;
    }
       
    public void setFiles(ArrayList<Integer> files) {
        this.files = files;
    }
    
    public double[] getCentroid() {
        return this.centroid;
    }
    
    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }
}
