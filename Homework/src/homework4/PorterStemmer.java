package homework4;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.*;

public class PorterStemmer {

   // a single consonant
   private static final String c = "[^aeiou]";
   // a single vowel
   private static final String v = "[aeiouy]";

   // a sequence of consonants; the second/third/etc consonant cannot be 'y'
   private static final String C = c + "[^aeiouy]*";
   // a sequence of vowels; the second/third/etc cannot be 'y'
   private static final String V = v + "[aeiou]*";

   // this regex pattern tests if the token has measure > 0 [at least one VC].
   private static final Pattern mGr0 = Pattern.compile("^(" + C + ")?" + V + C);

   // add more Pattern variables for the following patterns:
   // m equals 1: token has measure == 1
   private static final Pattern mE1 = Pattern.compile(mGr0 + "(" + V + ")?$");
   // m greater than 1: token has measure > 1
   private static final Pattern mGr1 = Pattern.compile(mGr0 + V + C);
   // vowel: token has a vowel after the first (optional) C
   private static final Pattern vAC = Pattern.compile("^(" + C + ")?" + v); 
   // double consonant: token ends in two consonants that are the same, 
   //			unless they are L, S, or Z. (look up "backreferencing" to help 
   //			with this)
   private static final Pattern dC = Pattern.compile("[^lsz]$\1");
   // m equals 1, cvc: token is in Cvc form, where the last c is not w, x,
   //			or y.
   private static final Pattern mE1CVC = Pattern.compile(C + V + "[^aeiouwxy]{1}$");
   
   // NEED TO DO 2D ARRAY
   private static final HashMap<String, String> step2Suffix = new HashMap<>();
   private static final HashMap<String, String> step3Suffix = new HashMap<>();
   private static final HashMap<String, String> step4Suffix = new HashMap<>();
   
   public static void main(String[] args) {
	   Scanner scan = new Scanner(System.in);
	   String word = scan.next();
	   setupStep2Map();
           setupStep3Map();
           setupStep4Map();
           
	   while(word.compareTo("quit") != 0) {
		System.out.println(processToken(word));
		word = scan.next();
	   }
	   
	   scan.close();
   }
   
   public static String processToken(String token) {
      if (token.length() < 3) {
         return token; // token must be at least 3 chars
      }
      // step 1a
      if (token.endsWith("sses") || token.endsWith("ies")) {
         token = token.substring(0, token.length() - 2);
      }
      // program the other steps in 1a. 
      // note that Step 1a.3 implies that there is only a single 's' as the 
      //	suffix; ss does not count. you may need a regex pattern here for 
      // "not s followed by s".
      
      else if(token.endsWith("s")) {
          if(token.charAt(token.length() - 2) != 's') {
              token = token.substring(0, token.length() - 1);
          }
      }
            
      // step 1b
      boolean doStep1bb = false;
      //		step 1b
      if (token.endsWith("eed")) { // 1b.1
         // token.substring(0, token.length() - 3) is the stem prior to "eed".
         // if that has m>0, then remove the "d".
         String stem = token.substring(0, token.length() - 3);
         if (mGr0.matcher(stem).find()) { // if the pattern matches the stem
            token = stem + "ee";
         }
      }
      // program the rest of 1b. set the boolean doStep1bb to true if Step 1b* 
      // should be performed.

      else if(token.endsWith("ed")) {
          String stem = token.substring(0, token.length() - 2);
          
          if(vAC.matcher(stem).find()) {
              token = stem;
              doStep1bb = true;
          }
      }
      
      else if(token.endsWith("ing")) {
          String stem = token.substring(0, token.length() - 3);
          
          if(vAC.matcher(stem).find()) {
              token = stem;
              doStep1bb = true;
          }
      }

      // step 1b*, only if the 1b.2 or 1b.3 were performed.
      if (doStep1bb) {
         if (token.endsWith("at") || token.endsWith("bl")
          || token.endsWith("iz")) {

            token = token + "e";
         }
         // use the regex patterns you wrote for 1b*.4 and 1b*.5
         
         else if(dC.matcher(token).find()) {
             token = token.substring(0, token.length() - 1);
         }
         
         else if(mE1CVC.matcher(token).find()) {
             token = token + "e";
         }
      }

      // step 1c
      // program this step. test the suffix of 'y' first, then test the 
      // condition *v* on the stem.

      if(token.endsWith("y")) {
          String stem = token.substring(0, token.length() - 1);
          if(vAC.matcher(stem).find()) {
              token = stem + "i";
          }
      }
         
      // step 2
      // program this step. for each suffix, see if the token ends in the 
      // suffix. 
      //    * if it does, extract the stem, and do NOT test any other suffix.
      //    * take the stem and make sure it has m > 0.
      //        * if it does, complete the step and do not test any others.
      //          if it does not, attempt the next suffix.

      // you may want to write a helper method for this. a matrix of
      // "suffix"/"replacement" pairs might be helpful. It could look like
      // string[][] step2pairs = {  new string[] {"ational", "ate"}, 
      // new string[] {"tional", "tion"}, ....
      token = stepMapHelper(token, 2, mGr0);
            
      // step 3
      // program this step. the rules are identical to step 2 and you can use
      // the same helper method. you may also want a matrix here.
      token = stepMapHelper(token, 3, mGr0);

      // step 4
      // program this step similar to step 2/3, except now the stem must have
      // measure > 1.
      // note that ION should only be removed if the suffix is SION or TION, 
      // which would leave the S or T.
      // as before, if one suffix matches, do not try any others even if the 
      // stem does not have measure > 1.
      token = stepMapHelper(token, 4, mGr1);

      // step 5
      // program this step. you have a regex for m=1 and for "Cvc", which
      // you can use to see if m=1 and NOT Cvc.
      // all your code should change the variable token, which represents
      // the stemmed term for the token.
      if(token.endsWith("e")) {
          String stem = token.substring(0, token.length() - 1);
          
          if(mGr1.matcher(stem).find()) {
              token = stem;
          }
          //CHECK IF RIGHT
          if(mE1.matcher(stem).find() && !mE1CVC.matcher(stem).find()) {
              token = stem;
          }
      }
      if(token.endsWith("ll")) {
          if(mGr1.matcher(token).find()) {
              token = token.substring(0, token.length() - 1);
          }
      }
      
      return token;
   }
   
   private static String stepMapHelper(String token, int step, Pattern type) {
        HashMap<String, String> map = new HashMap<>();
        
        if(step == 2) {
            map = step2Suffix;
        }
        else if(step == 3) {
            map = step3Suffix;
        }
        else if(step == 4) {
            map = step4Suffix;
        }
      
        for(String key : map.keySet()) {
            if(token.endsWith(key)) {
                System.out.println(key);
                if(step == 4 && key.compareTo("ion") == 0 && token.length() > 3) {
                    if(token.charAt(token.length()-4) != 's' && token.charAt(token.length()-4) != 't') {
                        break;
                    }
                }
                
                String stem = token.substring(0, token.length() - key.length());
              
                if(type.matcher(stem).find()) {
                    token = stem + map.get(key);
                }
                    
                break;
            }
        }
        
        return token;
   }
   
   private static void setupStep2Map() {
       step2Suffix.put("ational", "ate");
       step2Suffix.put("tional", "tion");
       step2Suffix.put("enci", "ence");
       step2Suffix.put("anci", "ance");
       step2Suffix.put("izer", "ize");
       step2Suffix.put("abli", "able");
       step2Suffix.put("alli", "al");
       step2Suffix.put("entli", "ent");
       step2Suffix.put("eli", "e");
       step2Suffix.put("ousli", "ous");
       step2Suffix.put("ization", "ize");
       step2Suffix.put("ation", "ate");
       step2Suffix.put("ator", "ate");
       step2Suffix.put("alism", "al");
       step2Suffix.put("iveness", "ive");
       step2Suffix.put("fulness", "ful");
       step2Suffix.put("ousness", "ous");
       step2Suffix.put("aliti", "al");
       step2Suffix.put("iviti", "ive");
       step2Suffix.put("biliti", "ble");
   }
   
   private static void setupStep3Map() {
       step3Suffix.put("icate", "ic");
       step3Suffix.put("ative", "");
       step3Suffix.put("alize", "al");
       step3Suffix.put("iciti", "ic");
       step3Suffix.put("ical", "ic");
       step3Suffix.put("ful", "");
       step3Suffix.put("ness", "");
   }
   
   private static void setupStep4Map() {
       step4Suffix.put("al", "");
       step4Suffix.put("ance", "");
       step4Suffix.put("ence", "");
       step4Suffix.put("er", "");
       step4Suffix.put("ic", "");
       step4Suffix.put("able", "");
       step4Suffix.put("ible", "");
       step4Suffix.put("ant", "");
       step4Suffix.put("ement", "");
       step4Suffix.put("ment", "");
       step4Suffix.put("ent", "");
       step4Suffix.put("ion", "");
       step4Suffix.put("ou", "");
       step4Suffix.put("ism", "");
       step4Suffix.put("ate", "");
       step4Suffix.put("iti", "");
       step4Suffix.put("ous", "");
       step4Suffix.put("ive", "");
       step4Suffix.put("ize", "");
   }
}
