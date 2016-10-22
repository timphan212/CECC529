package searchengineproject;

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

    public DiskInvertedIndex(String path) throws IOException {
        try {
            mPath = path;
            mVocabList = new RandomAccessFile(new File(path, "vocab.bin"), "r");
            mPostings = new RandomAccessFile(new File(path, "postings.bin"), "r");
            mVocabTable = readVocabTable(path);
            fileNames = readAllFileNames(path);
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

    public ArrayList<PositionalPosting> GetPostings(String term,
            boolean positions) {
        long postingsPosition = binarySearchVocabulary(term);

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

    private long binarySearchVocabulary(String term) {
        // do a binary search over the vocabulary, using the vocabTable and the file vocabList.
        int i = 0, j = mVocabTable.length / 2 - 1;
        while (i <= j) {
            try {
                int m = (i + j) / 2;
                long vListPosition = mVocabTable[m * 2];
                int termLength;
                if (m == mVocabTable.length / 2 - 1) {
                    termLength = (int) (mVocabList.length() - mVocabTable[m * 2]);
                } else {
                    termLength = (int) (mVocabTable[(m + 1) * 2] - vListPosition);
                }

                mVocabList.seek(vListPosition);

                byte[] buffer = new byte[termLength];
                mVocabList.read(buffer, 0, termLength);
                String fileTerm = new String(buffer, "ASCII");

                int compareValue = term.compareTo(fileTerm);
                if (compareValue == 0) {
                    // found it!
                    return mVocabTable[m * 2 + 1];
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

    private static long[] readVocabTable(String indexName) {
        try {
            long[] vocabTable;

            RandomAccessFile tableFile = new RandomAccessFile(
                    new File(indexName, "vocabTable.bin"),
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

    public int getTermCount() {
        return mVocabTable.length / 2;
    }

    private static ArrayList<String> readAllFileNames(String path) throws IOException {
        ArrayList<String> names = new ArrayList<>();
        final Path currentWorkingPath = Paths.get(path).toAbsolutePath();
        
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
                    // we have found a .json file; add its name to the fileName
                    // list, then index the file and increase the document ID
                    // counter.
                    names.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            public FileVisitResult visitFileFailed(Path file,
                    IOException e) {

                return FileVisitResult.CONTINUE;
            }

        });
        
        return names;
    }
    
    public int getDocumentCount() {
        return fileNames.size();
    }
    
    public String getFileNames(int docID) {
        return fileNames.get(docID);
    }
    
    public ArrayList<String> getTerms() {
        int termLength = 0;
        ArrayList<String> terms = new ArrayList<>();
        long vocabPos = 0;
        byte[] buffer;
        
        for(int i = 0; i < mVocabTable.length; i+=2) {
            try {
                vocabPos = mVocabTable[i];
                mVocabList.seek(vocabPos);
                
                if(i == mVocabTable.length - 2) {
                    termLength = (int) (mVocabList.length() - mVocabTable[i]);
                }
                else {
                    termLength = (int) (mVocabTable[i+2] - vocabPos);
                }
                
                buffer = new byte[termLength];
                mVocabList.read(buffer, 0, termLength);
                terms.add(new String(buffer, "ASCII"));
            } catch (IOException ex) {
                Logger.getLogger(DiskInvertedIndex.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
        return terms;
    }
}
