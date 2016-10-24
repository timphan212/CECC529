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

/**
 *
 * @author Tim
 */
public class DirectoryBiwordIndex {

    public static void buildIndexForDirectory(String folder) {
        BiwordIndex index = new BiwordIndex();

        // Index the directory using a naive index
        indexFiles(folder, index);

        // at this point, "index" contains the in-memory inverted index 
        // now we save the index to disk, building three files: the postings
        // index, the vocabulary list, and the vocabulary table.
        // the array of terms
        String[] dictionary = index.getTerms();
        // an array of positions in the vocabulary file
        long[] vocabPositions = new long[dictionary.length];
        DirectoryIndex.buildVocabFile(folder, dictionary, vocabPositions,
                "biwordVocab.bin");
        buildPostingsFile(folder, index, dictionary, vocabPositions);
    }
    
    /**
     * Builds the postings.bin file for the indexed directory, using the given
     * NaiveInvertedIndex of that directory.
     */
    private static void buildPostingsFile(String folder, BiwordIndex index,
            String[] dictionary, long[] vocabPositions) {
        FileOutputStream postingsFile = null;
        
        try {
            postingsFile = new FileOutputStream(
                    new File(folder, "biwordPostings.bin")
            );

            // simultaneously build the vocabulary table on disk, mapping a
            // term index to a file location in the postings file.
            FileOutputStream vocabTable = new FileOutputStream(
                    new File(folder, "biwordVocabTable.bin")
            );

            // the first thing we must write to the vocabTable file is the
            // number of vocab terms.
            byte[] tSize = ByteBuffer.allocate(4)
                    .putInt(dictionary.length).array();
            vocabTable.write(tSize, 0, tSize.length);
            int vocabI = 0;

            for (String s : dictionary) {
                ArrayList<Integer> fileList = index.getFileList(s);
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
                        .putInt(fileList.size()).array();
                postingsFile.write(docFreqBytes, 0, docFreqBytes.length);

                int lastDocId = 0;

                for (Integer docID : fileList) {
                    // write the document id
                    byte[] docIdBytes = ByteBuffer.allocate(4)
                            .putInt(docID - lastDocId).array();
                    postingsFile.write(docIdBytes, 0, docIdBytes.length);
                    lastDocId = docID;
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
    
    private static void indexFiles(String folder,
            final BiwordIndex index) {
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
                        indexFile(file.toFile(), mDocumentID, index);
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
            BiwordIndex index) {
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.

        try {
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
            // string to keep track of the previous word for biword
            String prevWord = "";

            // loop until the term returned is null
            while (term != null) {
                // skip the term if the word is empty
                if (term.compareTo("") != 0) {
                    // save the first word in the file as the previous word
                    if (counter == 0) {
                        prevWord = term;
                    } 
                    // stem the previous token and the current token then add to
                    // the biword index and set the previous word to the current
                    // word
                    else {
                        index.addTerm(PorterStemmer.processToken(prevWord)
                                + " " + PorterStemmer
                                .processToken(term), docID);
                        prevWord = term;
                    }
                }
                // get the next token and increment the position counter
                term = tokeStream.nextToken();
                counter++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
