package homework5;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class DiskEngine {

   public static void main(String[] args) throws IOException {
      Scanner scan = new Scanner(System.in);

      System.out.println("Menu:");
      System.out.println("1) Build index");
      System.out.println("2) Read and query index");
      System.out.println("Choose a selection:");
      int menuChoice = scan.nextInt();
      scan.nextLine();

      switch (menuChoice) {
         case 1:
            System.out.println("Enter the name of a directory to index: ");
            String folder = scan.nextLine();

            IndexWriter writer = new IndexWriter(folder);
            writer.buildIndex();
            break;

         case 2:
            System.out.println("Enter the name of an index to read:");
            String indexName = scan.nextLine();

            DiskInvertedIndex index = new DiskInvertedIndex(indexName);

            while (true) {
               System.out.println("Enter one or more search terms, separated " +
                "by spaces:");
               String input = scan.nextLine();

               if (input.equals("EXIT")) {
                  break;
               }

               ArrayList<PositionalPosting> postingsList = index.GetPostings(
                PorterStemmer.processToken(input.toLowerCase())
               );

               if (postingsList == null) {
                  System.out.println("Term not found");
               }
               else {
                  System.out.println("Docs: ");
                  for (PositionalPosting post : postingsList) {
                     System.out.println(index.getFileNames(post.getDocID()));
                  }
                  System.out.println(postingsList.size());
                  System.out.println();
                  System.out.println();
               }
            }

            break;
      }
   }
}
