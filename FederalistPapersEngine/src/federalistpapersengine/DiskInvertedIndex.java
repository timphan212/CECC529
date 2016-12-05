package federalistpapersengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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

public class DiskInvertedIndex {

    private String mPath;
    private RandomAccessFile mVocabList;
    private RandomAccessFile mPostings;
    private long[] mVocabTable;
    private ArrayList<String> fileNames = new ArrayList<>();
    private RandomAccessFile docWeights;

    public DiskInvertedIndex(String path) throws IOException {
        try {
            mPath = path;
            mVocabList = new RandomAccessFile(new File(path, "vocab.bin"), "r");
            mPostings = new RandomAccessFile(new File(path, "postings.bin"), "r");
            mVocabTable = readVocabTable(path, "vocabTable.bin");
            fileNames = readAllFileNames(path + "\\ALL");
            fileNames.addAll(readAllFileNames(path + "\\HAMILTON OR MADISON"));
            docWeights = new RandomAccessFile(new File(path, "docWeights.bin"), "r");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Returns an array list of positional postings containing the positions
     * @param postings postings file
     * @param postingsPosition position inside the postings file
     * @return array list of positional posting
     */
    private static ArrayList<PositionalPosting> readPositionalPostings (
            RandomAccessFile postings, long postingsPosition) {
        try {
            // seek to the position in the file where the postings start.
            postings.seek(postingsPosition);

            // read the 4 bytes for the document frequency
            byte[] buffer = new byte[4];
            postings.read(buffer, 0, buffer.length);
            
            // use ByteBuffer to convert the 4 bytes into an int.
            int documentFrequency = ByteBuffer.wrap(buffer).getInt();
            
            int lastDocId = 0, lastPosId = 0;
            ArrayList<PositionalPosting> posPostList = new ArrayList<>();
            
            for (int i = 0; i < documentFrequency; i++) {
                // list of positions
                ArrayList<Integer> positions = new ArrayList<>();

                // read the document id
                byte[] docBuffer = new byte[4];
                postings.read(docBuffer, 0, docBuffer.length);
                int documentID = ByteBuffer.wrap(docBuffer).getInt() + lastDocId;
                lastDocId = documentID;

                // read the term frequency
                byte[] tfBuffer = new byte[4];
                postings.read(tfBuffer, 0, tfBuffer.length);
                int termFrequency = ByteBuffer.wrap(tfBuffer).getInt();
                
                // read the positions for the term in the document
                for (int j = 0; j < termFrequency; j++) {
                    byte[] posBuffer = new byte[4];
                    postings.read(posBuffer, 0, posBuffer.length);
                    int pos = ByteBuffer.wrap(posBuffer).getInt() + lastPosId;
                    lastPosId = pos;
                    positions.add(pos);
                }

                // create the positional posting list
                PositionalPosting posPost = new PositionalPosting(documentID,
                        termFrequency, positions);
                posPostList.add(posPost);
            }

            return posPostList;
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }
    
    /**
     * Returns an array list of positional postings without the positions
     * @param postings posting files
     * @param postingsPosition position inside the posting files
     * @return array list of positional postings
     */
    private static ArrayList<PositionalPosting> readPostings (
            RandomAccessFile postings, long postingsPosition) {
        try {
            // seek to the position in the file where the postings start.
            postings.seek(postingsPosition);

            // read the 4 bytes for the document frequency
            byte[] buffer = new byte[4];
            postings.read(buffer, 0, buffer.length);
            
            // use ByteBuffer to convert the 4 bytes into an int.
            int documentFrequency = ByteBuffer.wrap(buffer).getInt();
            
            int lastDocId = 0;
            ArrayList<PositionalPosting> posPostList = new ArrayList<>();
            
            for (int i = 0; i < documentFrequency; i++) {
                // read the document id
                byte[] docBuffer = new byte[4];
                postings.read(docBuffer, 0, docBuffer.length);
                int documentID = ByteBuffer.wrap(docBuffer).getInt() + lastDocId;
                lastDocId = documentID;
                
                // read the term frequency
                byte[] tfBuffer = new byte[4];
                postings.read(tfBuffer, 0, tfBuffer.length);
                int termFrequency = ByteBuffer.wrap(tfBuffer).getInt();
                
                postings.skipBytes(termFrequency*4);

                // create the positional posting list
                PositionalPosting posPost = new PositionalPosting(documentID,
                        termFrequency);
                posPostList.add(posPost);
            }

            return posPostList;
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }
    
    /**
     * Retrieve the positional postings of the term
     * @param term string to search for in the posting file
     * @param positions flag to check if positions are needed
     * @return array list of positional postings for the term
     */
    public ArrayList<PositionalPosting> GetPostings(String term,
            boolean positions) {
        long postingsPosition = binarySearchVocabulary(term, mVocabTable,
                mVocabList);

        if (postingsPosition >= 0) {
            if(positions == true) {
                return readPositionalPostings(mPostings, postingsPosition);
            }
            else {
                return readPostings(mPostings, postingsPosition);
            }
            
        }

        return null;
    }
    
    /**
     * Do a binary search to find the location of the vocab
     * @param term the term to look for
     * @param vocabTable the array containing the locations of vocabs
     * @param vocabList the file of vocab words
     * @return the long value of the location
     */
    private long binarySearchVocabulary(String term, long[] vocabTable,
            RandomAccessFile vocabList) {
        // do a binary search over the vocabulary, using the vocabTable and the file vocabList.
        int i = 0, j = vocabTable.length / 2 - 1;
        while (i <= j) {
            try {
                int m = (i + j) / 2;
                long vListPosition = vocabTable[m * 2];
                int termLength;
                if (m == vocabTable.length / 2 - 1) {
                    termLength = (int) (vocabList.length() - vocabTable[m * 2]);
                } else {
                    termLength = (int) (vocabTable[(m + 1) * 2] - vListPosition);
                }

                vocabList.seek(vListPosition);

                byte[] buffer = new byte[termLength];
                vocabList.read(buffer, 0, termLength);
                String fileTerm = new String(buffer, "ASCII");

                int compareValue = term.compareTo(fileTerm);
                if (compareValue == 0) {
                    // found it!
                    return vocabTable[m * 2 + 1];
                } else if (compareValue < 0) {
                    j = m - 1;
                } else {
                    i = m + 1;
                }
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
        return -1;
    }

    /**
     * Creates an array of long values that represent the locations of each
     * word in the file
     * @param indexName the name of the location
     * @param fileName the file name of the index, biword or positional
     * @return an array of longs representing the vocab positions
     */
    private static long[] readVocabTable(String indexName, String fileName) {
        try {
            long[] vocabTable;

            RandomAccessFile tableFile = new RandomAccessFile(
                    new File(indexName, fileName),
                    "r");

            byte[] byteBuffer = new byte[4];
            tableFile.read(byteBuffer, 0, byteBuffer.length);

            int tableIndex = 0;
            vocabTable = new long[ByteBuffer.wrap(byteBuffer).getInt() * 2];
            byteBuffer = new byte[8];

            while (tableFile.read(byteBuffer, 0, byteBuffer.length) > 0) {
                vocabTable[tableIndex] = ByteBuffer.wrap(byteBuffer).getLong();
                tableIndex++;
            }
            tableFile.close();
            return vocabTable;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    /**
     * Retrieves the number of terms in the positional index
     * @return an integer representing the count
     */
    public int getTermCount() {
        return mVocabTable.length / 2;
    }
   
    /**
     * Loops through all the files to get an arraylist of all the file names
     * @param path a string representing the directory location
     * @return an arraylist of strings containing the file names
     * @throws IOException 
     */
    public static ArrayList<String> readAllFileNames(String path) throws IOException {
        final Path currentWorkingPath = Paths.get(path).toAbsolutePath();
        ArrayList<String> files = new ArrayList<>();
        
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
                if (file.toString().endsWith(".txt")) {
                    // we have found a .json file; add its name to the fileName
                    // list, then index the file and increase the document ID
                    // counter.
                    files.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            public FileVisitResult visitFileFailed(Path file,
                    IOException e) {

                return FileVisitResult.CONTINUE;
            }

        });
        
        return files;
    }
    
    /**
     * Get the total document count
     * @return an integer representing the count
     */
    public int getDocumentCount() {
        return fileNames.size();
    }
    
    /**
     * Get the actual file name in the directory
     * @param docID the document id to search for
     * @return a string representing the file name
     */
    public String getFileNames(int docID) {
        return fileNames.get(docID);
    }
    
    /**
     * Get the terms in the positional index
     * @return an arraylist of string representing the terms in the index
     */
    public ArrayList<String> getPositionalIndexTerms() {
        return getTerms(mVocabTable, mVocabList);
    }
    
    /**
     * Gets an arraylist of string representing the terms depending on 
     * the parameters passed in
     * @param vocabTable the arrays of long for the position
     * @param vocabFile the file to open and read from
     * @return an arraylist of string representing the terms
     */
    private ArrayList<String> getTerms(long[] vocabTable, RandomAccessFile vocabFile) {
        int termLength = 0;
        ArrayList<String> terms = new ArrayList<>();
        long vocabPos = 0;
        byte[] buffer;
        
        for(int i = 0; i < vocabTable.length; i+=2) {
            try {
                vocabPos = vocabTable[i];
                vocabFile.seek(vocabPos);
                
                if(i == vocabTable.length - 2) {
                    termLength = (int) (vocabFile.length() - vocabTable[i]);
                }
                else {
                    termLength = (int) (vocabTable[i+2] - vocabPos);
                }
                
                buffer = new byte[termLength];
                vocabFile.read(buffer, 0, termLength);
                terms.add(new String(buffer, "ASCII"));
            } catch (IOException ex) {
                Logger.getLogger(DiskInvertedIndex.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
        return terms;
    }
    
    /**
     * Gets the document weight for the specified document id
     * @param documentID the document id to look for
     * @param docWeightsFile the document weights file
     * @param offset the offset to seek by
     * @return a double representing the doc weight
     */
    private double findDocWeight(int documentID, 
            RandomAccessFile docWeightsFile, int offset) {
        try {
            docWeightsFile.seek(documentID*32+offset);
            byte[] buffer = new byte[8];
            docWeightsFile.read(buffer, 0, buffer.length);
            
            return ByteBuffer.wrap(buffer).getDouble();
        } catch (IOException ex) {
            Logger.getLogger(DiskInvertedIndex.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return 0.0;
    }
    
    /**
     * Get the document weight for the document id
     * @param documentID the document id to look for
     * @return a double representing the doc weight
     */
    public double getDocWeight(int documentID) {
        return findDocWeight(documentID, docWeights, 0);
    }
    
    /**
     * Get the document length for the specified document id
     * @param documentID the document id to look for
     * @return a double representing the doc length
     */
    public double getDocLength(int documentID) {
        return findDocWeight(documentID, docWeights, 8);
    }
    
    /**
     * Get the document byte size for the specified document id
     * @param documentID the document id to look for
     * @return a double representing the doc length
     */
    public double getDocByteSize(int documentID) {
        return findDocWeight(documentID, docWeights, 16);
    }
    
    /**
     * Get the average term frequency for specified document id
     * @param documentID the document id to look for
     * @return a double representing the average term frequency
     */
    public double getAverageTermFreq(int documentID) {
        return findDocWeight(documentID, docWeights, 24);
    }
    
    /**
     * Get the average document length of the whole corpus
     * @return a double representing the average document length
     */
    public double getAverageDocLength() {
        return findDocWeight(fileNames.size(), docWeights, 0);
    }
    
    /**
     * Get a list of all the file names
     * @return 
     */
    public ArrayList<String> getFileNameList() {
        return this.fileNames;
    }
}
