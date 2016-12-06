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
public class MadisonDocuments {
    private ArrayList<Integer> files;
    private double[] centroid;
    private double[] itc;
    private ArrayList<Integer> rocchioFileSet;
    private HashMap<String, Integer> ftcMap;
    private int ftcSum;
    
    public MadisonDocuments(ArrayList<Integer> files) {
        this.files = files;
        this.rocchioFileSet = files;
        ftcMap = new HashMap<>();
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

    public double[] getItc() {
        return itc;
    }

    public void setItc(double[] itc) {
        this.itc = itc;
    }

    public ArrayList<Integer> getRocchioFileSet() {
        return rocchioFileSet;
    }

    public void setRocchioFileSet(ArrayList<Integer> rocchioFileSet) {
        this.rocchioFileSet = rocchioFileSet;
    }

    public HashMap<String, Integer> getFtcMap() {
        return ftcMap;
    }

    public void setFtcMap(HashMap<String, Integer> ftcMap) {
        this.ftcMap = ftcMap;
    }

    public int getFtcSum() {
        return ftcSum;
    }

    public void setFtcSum(int ftcSum) {
        this.ftcSum = ftcSum;
    }
    
    
}
