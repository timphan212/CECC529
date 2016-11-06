/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Timothy
 */
public class DirectoryIndex {
    private static ArrayList<Integer> docLengthList;
    /**
     * Builds the normal NaiveInvertedIndex for the folder.
     */
    public static void buildIndexForDirectory(String folder) {
        PositionalInvertedIndex index = new PositionalInvertedIndex();
        docLengthList = new ArrayList<>();
        // Index the directory using a naive index
        indexFiles(folder, index);
        addAvgDocLength(folder);
        // at this point, "index" contains the in-memory inverted index 
        // now we save the index to disk, building three files: the postings
        // index, the vocabulary list, and the vocabulary table.
        // the array of terms
        String[] dictionary = index.getTerms();
        // an array of positions in the vocabulary file
        long[] vocabPositions = new long[dictionary.length];

        buildVocabFile(folder, dictionary, vocabPositions, "vocab.bin");
        buildPostingsFile(folder, index, dictionary, vocabPositions);
    }

    /**
     * Builds the postings.bin file for the indexed directory, using the given
     * NaiveInvertedIndex of that directory.
     */
    private static void buildPostingsFile(String folder,
            PositionalInvertedIndex index, String[] dictionary,
            long[] vocabPositions) {
        FileOutputStream postingsFile = null;
        
        try {
            postingsFile = new FileOutputStream(
                    new File(folder, "postings.bin")
            );

            // simultaneously build the vocabulary table on disk, mapping a
            // term index to a file location in the postings file.
            FileOutputStream vocabTable = new FileOutputStream(
                    new File(folder, "vocabTable.bin")
            );

            // the first thing we must write to the vocabTable file is the
            // number of vocab terms.
            byte[] tSize = ByteBuffer.allocate(4)
                    .putInt(dictionary.length).array();
            vocabTable.write(tSize, 0, tSize.length);
            int vocabI = 0;

            for (String s : dictionary) {
                // for each String in dictionary, retrieve its postings.
                ArrayList<PositionalPosting> postings = index
                        .getPositionalPosting(s);

                // write the vocab table entry for this term: the byte location
                // of the term in the vocab list file, and the byte location of
                // the postings for the term in the postings file.
                byte[] vPositionBytes = ByteBuffer.allocate(8)
                        .putLong(vocabPositions[vocabI]).array();
                vocabTable.write(vPositionBytes, 0, vPositionBytes.length);

                byte[] pPositionBytes = ByteBuffer.allocate(8)
                        .putLong(postingsFile.getChannel().position()).array();
                vocabTable.write(pPositionBytes, 0, pPositionBytes.length);

                // write the postings file for this term. first, the document
                // frequency for the term, then the document IDs,
                // encoded as gaps.
                byte[] docFreqBytes = ByteBuffer.allocate(4)
                        .putInt(postings.size()).array();
                postingsFile.write(docFreqBytes, 0, docFreqBytes.length);

                int lastDocId = 0, lastPosId = 0;

                for (PositionalPosting post : postings) {
                    // write the document id
                    byte[] docIdBytes = ByteBuffer.allocate(4)
                            .putInt(post.getDocID() - lastDocId).array();
                    postingsFile.write(docIdBytes, 0, docIdBytes.length);
                    lastDocId = post.getDocID();
                    // write term frequency for document
                    byte[] termFreqBytes = ByteBuffer.allocate(4)
                            .putInt(post.getPositions().size()).array();
                    postingsFile.write(termFreqBytes, 0, termFreqBytes.length);
                    // write each position of term
                    for (Integer position : post.getPositions()) {
                        byte[] posIdBytes = ByteBuffer.allocate(4)
                                .putInt(position - lastPosId).array();
                        postingsFile.write(posIdBytes, 0, posIdBytes.length);
                        lastPosId = position;
                    }
                }

                vocabI++;
            }
            vocabTable.close();
            postingsFile.close();
        } 
        catch (IOException ex) {System.out.println(ex.toString());}
        finally {
            try {
                postingsFile.close();
            }
            catch (IOException ex) {System.out.println(ex.toString());}
        }
    } 

    public static void buildVocabFile(String folder, String[] dictionary,
            long[] vocabPositions, String fileName) {
        OutputStreamWriter vocabList = null;

        try {
            // first build the vocabulary list: a file of each vocab
            // word concatenated together. also build an array associating 
            // each term with its byte location in this file.
            int vocabI = 0;
            vocabList = new OutputStreamWriter(
                    new FileOutputStream(new File(folder, fileName)), "ASCII"
            );

            int vocabPos = 0;
            for (String vocabWord : dictionary) {
                // for each String in dictionary, save the byte position where
                // that term will start in the vocab file.
                vocabPositions[vocabI] = vocabPos;
                vocabList.write(vocabWord); // then write the String
                vocabI++;
                vocabPos += vocabWord.length();
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } finally {
            try {
                vocabList.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    private static void indexFiles(String folder,
            final PositionalInvertedIndex index) {
        final Path currentWorkingPath = Paths.get(folder).toAbsolutePath();
        
        try {
            Files.walkFileTree(currentWorkingPath,
                    new SimpleFileVisitor<Path>() {
                int mDocumentID = 0;

                public FileVisitResult preVisitDirectory(Path dir,
                        BasicFileAttributes attrs) {
                    // make sure we only process the current working directory
                    if (currentWorkingPath.equals(dir)) {
                        return FileVisitResult.CONTINUE;
                    }
                    return FileVisitResult.SKIP_SUBTREE;
                }

                public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attrs) {
                    // only process .txt files
                    if (file.toString().endsWith(".json")) {
                        // we have found a .txt file; add its name to the
                        // fileName list, then index the file and increase the
                        // document ID counter.
                        indexFile(file.toFile(), mDocumentID, index, folder);
                        mDocumentID++;
                    }
                    return FileVisitResult.CONTINUE;
                }

                // don't throw exceptions if files are locked/other errors occur
                public FileVisitResult visitFileFailed(Path file,
                        IOException e) {

                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Indexes a file by reading a series of tokens from the file, treating each
     * token as a term, and then adding the given document's ID to the inverted
     * index for the term.
     *
     * @param file a File object for the document to index.
     * @param docID the integer ID of the current document, needed when indexing
     * each term from the document.
     */
    private static void indexFile(File file, int docID,
            PositionalInvertedIndex index, String folder) {
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.

        try {
            HashMap<String, Integer> termFreqMap = new HashMap<>();
            // use GSON to grab the body tag from the document
            Gson gson = new Gson();
            // open the file with gson
            NPSDocument npsDoc = gson.fromJson(new FileReader(file),
                    NPSDocument.class);
            // pass the body string to the token stream
            SimpleTokenStream tokeStream = new SimpleTokenStream(npsDoc.body);
            // get the next token in the stream
            String term = tokeStream.nextToken();
            // counter to keep track of position
            int counter = 0;
            String stemmedTerm = "";
            
            // loop until the term returned is null
            while (term != null) {
                // skip the term if the word is empty
                if (term.compareTo("") != 0) {
                    // if the term contains a hyphen then remove the hyphen and
                    // stem then index the token, then split the string by
                    // the hyphen, stem and add each word individually
                    if (term.contains("-")) {
                        stemmedTerm = PorterStemmer
                                .processToken(term.replaceAll("-", ""));
                        index.addTerm(stemmedTerm, docID, counter);
                        updateTermFreqMap(stemmedTerm, termFreqMap);
                        String[] hyphenWord = term.split("-");

                        for (String word : hyphenWord) {
                            stemmedTerm = PorterStemmer.processToken(word);
                            index.addTerm(stemmedTerm, docID, counter);
                            updateTermFreqMap(stemmedTerm, termFreqMap);
                        }
                    } // stem and add the token to the positional inverted index
                    else {
                        stemmedTerm = PorterStemmer.processToken(term);
                        index.addTerm(stemmedTerm, docID, counter);
                        updateTermFreqMap(stemmedTerm, termFreqMap);
                    }
                }
                // get the next token and increment the position counter
                term = tokeStream.nextToken();
                counter++;
            }
            
            //write document weight to the folder
            buildWeightsFile(folder, termFreqMap, file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the document weights file
     * @param folder name of the directory th files are located
     * @param index the term frequency index
     */
    private static void buildWeightsFile(String folder, 
            HashMap<String, Integer> index, long fileSize) {
        try {
            double wdtSum = 0.0;
            double tfsum = 0.0;
            // create a file called docWeights.bin
            File weightsFile = new File(folder, "docWeights.bin");
            // creates an output stream
            FileOutputStream weightsFileOutput = null;
            
            // if the file already exists then append the doc weight to the end
            // of the file
            if(weightsFile.exists()) {
                weightsFileOutput = new FileOutputStream(weightsFile, true);
            }
            // otherwise create a new file
            else {
                weightsFile.createNewFile();
                weightsFileOutput = new FileOutputStream(weightsFile);
            }          
            
            // loop through each term in the hashmap and calculate wdt then
            // raise it to the power of 2
            for(Entry<String, Integer> term : index.entrySet()) {
                tfsum += term.getValue();                
                double wdt = 1 + Math.log((double)term.getValue());                
                wdtSum += Math.pow(wdt, 2);
            }
            
            // calculate ld by square rooting the sum of wdt^2
            double ld = Math.sqrt(wdtSum);
            // create a buffer to fit ld
            byte[] docWeightBytes = ByteBuffer.allocate(8)
                .putDouble(ld).array();
            // write the buffer to the file
            weightsFileOutput
                .write(docWeightBytes, 0, docWeightBytes.length);
            
            // get the number of tokens in the document
            double docLengthd = index.size();
            // add the number of tokens in the documents to the list
            docLengthList.add((int)docLengthd);
            // create a buffer to fit doc length
            byte[] docLengthBytes = ByteBuffer.allocate(8).putDouble(docLengthd)
                    .array();
            // write the buffer to the file
            weightsFileOutput.write(docLengthBytes, 0, docLengthBytes.length);
            
            // create a buffer to fit file size
            byte[] fileSizeBytes = ByteBuffer.allocate(8)
                    .putDouble((double)fileSize).array();
            // write the buffer to the file
            weightsFileOutput.write(fileSizeBytes, 0, fileSizeBytes.length);
            
            // calculate ave(tftd)
            double avgtf = tfsum/index.size();
            // create a buffer to fit ave(tftd)
            byte[] docAvgBytes = ByteBuffer.allocate(8).putDouble(avgtf)
                    .array();
            // write the buffer to the file
            weightsFileOutput.write(docAvgBytes, 0, docAvgBytes.length);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DirectoryIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DirectoryIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Updates the hashmap with the new term frequency
     * @param term a string variable representing the term
     * @param termFreqMap a hashmap which maps the term to the term frequency
     */
    private static void updateTermFreqMap(String term, 
            HashMap<String, Integer> termFreqMap) {
        // the hashmap contains the term already, therefore get the frequency
        // and increment it by one
        if(termFreqMap.containsKey(term)) {
            termFreqMap.replace(term, termFreqMap.get(term) + 1);
        }
        // the hashmap does not contain the term, insert the term and set the
        // frequency to 1
        else {
            termFreqMap.put(term, 1);
        }
    }

    private static void addAvgDocLength(String folder) {
        try {
            // create a file called docWeights.bin
            File weightsFile = new File(folder, "docWeights.bin");
            // creates an output stream
            FileOutputStream weightsFileOutput = null;

            // if the file already exists then append the doc weight to the end
            // of the file
            if (weightsFile.exists()) {
                weightsFileOutput = new FileOutputStream(weightsFile, true);
            } // otherwise create a new file
            else {
                weightsFile.createNewFile();
                weightsFileOutput = new FileOutputStream(weightsFile);
            }
            
            int docLengthSum = 0;
            
            for(int docLen : docLengthList) {
                docLengthSum += docLen;
            }
            
            double docLengthA = (double)docLengthSum/docLengthList.size();
            // create a buffer to fit doc length
            byte[] docLengthBytes = ByteBuffer.allocate(8).putDouble(docLengthA)
                    .array();
            // write the buffer to the file
            weightsFileOutput.write(docLengthBytes, 0, docLengthBytes.length);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DirectoryIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DirectoryIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
