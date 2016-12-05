/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package federalistpapersengine;

import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public class MadisonDocuments {
    private ArrayList<Integer> files;
    private double centroid;
    
    public MadisonDocuments(ArrayList<Integer> files) {
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
}
