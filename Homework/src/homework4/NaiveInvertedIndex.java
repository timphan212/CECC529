package homework4;


import java.util.*;

public class NaiveInvertedIndex {
   private HashMap<String, List<Integer>> mIndex;
   
   public NaiveInvertedIndex() {
      mIndex = new HashMap<String, List<Integer>>();
   }
   
   public void addTerm(String term, int documentID) {
      // TO-DO: add the term to the index hashtable. If the table does not have
      // an entry for the term, initialize a new ArrayList<Integer>, add the 
      // docID to the list, and put it into the map. Otherwise add the docID
      // to the list that already exists in the map, but ONLY IF the list does
      // not already contain the docID.
      
      if(!mIndex.containsKey(term)) {
    	  ArrayList<Integer> docList = new ArrayList<Integer>();
    	  docList.add(documentID);
    	  mIndex.put(term, docList);
      }
      else {
    	  List<Integer> docList = mIndex.get(term);
    	  
    	  if(docList.get(docList.size()-1) != documentID) {
    		  docList.add(documentID);
    		  mIndex.replace(term, docList);
    	  }
      }
      
   }
   
   public List<Integer> getPostings(String term) {
      // TO-DO: return the postings list for the given term from the index map.
      
      return mIndex.get(term);
   }
   
   public int getTermCount() {
      // TO-DO: return the number of terms in the index.
      
      return mIndex.size();
   }
   
   public String[] getDictionary() {
      // TO-DO: fill an array of Strings with all the keys from the hashtable.
      // Sort the array and return it.
	   
	  String[] terms = mIndex.keySet().toArray(new String[mIndex.size()]);
	  Arrays.sort(terms);
	  
      return terms;
   }
}
