package homework5;

import com.google.gson.Gson;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Writes an inverted indexing of a directory to disk.
 */
public class IndexWriter {

    private String mFolderPath;

    /**
     * Constructs an IndexWriter object which is prepared to index the given
     * folder.
     */
    public IndexWriter(String folderPath) {
        mFolderPath = folderPath;
    }

    /**
     * Builds and writes an inverted index to disk. Creates three files:
     * vocab.bin, containing the vocabulary of the corpus; postings.bin,
     * containing the postings list of document IDs; vocabTable.bin, containing
     * a table that maps vocab terms to postings locations
     */
    public void buildIndex() {
        buildIndexForDirectory(mFolderPath);
    }

    /**
     * Builds the normal NaiveInvertedIndex for the folder.
     */
    private static void buildIndexForDirectory(String folder) {
        PositionalInvertedIndex index = new PositionalInvertedIndex();

        // Index the directory using a naive index
        indexFiles(folder, index);

        // at this point, "index" contains the in-memory inverted index 
        // now we save the index to disk, building three files: the postings index,
        // the vocabulary list, and the vocabulary table.
        // the array of terms
        String[] dictionary = index.getTerms();
        // an array of positions in the vocabulary file
        long[] vocabPositions = new long[dictionary.length];

        buildVocabFile(folder, dictionary, vocabPositions);
        buildPostingsFile(folder, index, dictionary, vocabPositions);
    }

    /**
     * Builds the postings.bin file for the indexed directory, using the given
     * NaiveInvertedIndex of that directory.
     */
    private static void buildPostingsFile(String folder, PositionalInvertedIndex index,
            String[] dictionary, long[] vocabPositions) {
        FileOutputStream postingsFile = null;
        try {
            postingsFile = new FileOutputStream(
                    new File(folder, "postings.bin")
            );

            // simultaneously build the vocabulary table on disk, mapping a term index to a
            // file location in the postings file.
            FileOutputStream vocabTable = new FileOutputStream(
                    new File(folder, "vocabTable.bin")
            );

            // the first thing we must write to the vocabTable file is the number of vocab terms.
            byte[] tSize = ByteBuffer.allocate(4)
                    .putInt(dictionary.length).array();
            vocabTable.write(tSize, 0, tSize.length);
            int vocabI = 0;
            for (String s : dictionary) {
                // for each String in dictionary, retrieve its postings.
                ArrayList<PositionalPosting> postings = index.getPositionalPosting(s);

                // write the vocab table entry for this term: the byte location of the term in the vocab list file,
                // and the byte location of the postings for the term in the postings file.
                byte[] vPositionBytes = ByteBuffer.allocate(8)
                        .putLong(vocabPositions[vocabI]).array();
                vocabTable.write(vPositionBytes, 0, vPositionBytes.length);

                byte[] pPositionBytes = ByteBuffer.allocate(8)
                        .putLong(postingsFile.getChannel().position()).array();
                vocabTable.write(pPositionBytes, 0, pPositionBytes.length);

                // write the postings file for this term. first, the document frequency for the term, then
                // the document IDs, encoded as gaps.
                byte[] docFreqBytes = ByteBuffer.allocate(4)
                        .putInt(postings.size()).array();
                postingsFile.write(docFreqBytes, 0, docFreqBytes.length);
                
                int lastDocId = 0, lastPosId = 0;
                
                for(PositionalPosting post : postings) {
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
                    for(Integer position : post.getPositions()) {
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
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                postingsFile.close();
            } catch (IOException ex) {
            }
        }
    }

    private static void buildVocabFile(String folder, String[] dictionary,
            long[] vocabPositions) {
        OutputStreamWriter vocabList = null;
        try {
         // first build the vocabulary list: a file of each vocab word concatenated together.
            // also build an array associating each term with its byte location in this file.
            int vocabI = 0;
            vocabList = new OutputStreamWriter(
                    new FileOutputStream(new File(folder, "vocab.bin")), "ASCII"
            );

            int vocabPos = 0;
            for (String vocabWord : dictionary) {
                // for each String in dictionary, save the byte position where that term will start in the vocab file.
                vocabPositions[vocabI] = vocabPos;
                vocabList.write(vocabWord); // then write the String
                vocabI++;
                vocabPos += vocabWord.length();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.toString());
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

    private static void indexFiles(String folder, final PositionalInvertedIndex index) {
        final Path currentWorkingPath = Paths.get(folder).toAbsolutePath();

        try {
            Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {
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
                  // we have found a .txt file; add its name to the fileName list,
                        // then index the file and increase the document ID counter.
                        // System.out.println("Indexing file " + file.getFileName());
                        indexFile(file.toFile(), index, mDocumentID);
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
        } catch (IOException ex) {
            Logger.getLogger(IndexWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void indexFile(File fileName, PositionalInvertedIndex index,
            int documentID) {

        try {
            Gson gson = new Gson();
            NPSDocument npsDoc = gson.fromJson(new FileReader(fileName),
                    NPSDocument.class);
            SimpleTokenStream stream = new SimpleTokenStream(npsDoc.body);
            String term = stream.nextToken();
            int counter = 0;
            while(term != null) {
                
                if (term.contains("-")) {
                    index.addTerm(PorterStemmer.processToken(term
                            .replace("-", "")), documentID, counter);
                    String[] hyphenWord = term.split("-");

                    for (String word : hyphenWord) {
                        index.addTerm(PorterStemmer.processToken(word),
                                documentID, counter);
                    }
                }
                else {
                    index.addTerm(PorterStemmer.processToken(term),
                            documentID, counter);
                }
                term = stream.nextToken();
                counter++;
            }
        } catch (Exception ex) {
            System.out.println("Index File " + ex.toString());
        }
    }
}

class NPSDocument {

    String title;
    String body;
    String url;
}
