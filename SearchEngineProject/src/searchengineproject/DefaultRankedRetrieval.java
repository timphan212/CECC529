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
public class DefaultRankedRetrieval implements Strategy {
    @Override
    public ArrayList<String> rankingAlgorithm(String query) {
        System.out.println("default!");
        
        return null;
    }

    
}
