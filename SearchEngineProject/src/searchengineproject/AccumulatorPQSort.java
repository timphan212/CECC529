/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import java.util.Comparator;

/**
 *
 * @author Timothy
 */
public class AccumulatorPQSort implements Comparator<AccumulatorPosting> {

    @Override
    public int compare(AccumulatorPosting o1, AccumulatorPosting o2) {
        return Double.compare(o2.getAccumulator(), o1.getAccumulator());
    }
    
}
