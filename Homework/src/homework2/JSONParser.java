package homework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONParser {

	public static void main(String[] args) {
		readJSON("all-nps-sites.json");
		System.out.println("Finished.");
	}

	private static void readJSON (String fileName) {
		try {
			Gson gson = new Gson();
			NPSDocuments npsDocs = gson.fromJson(new FileReader(fileName), NPSDocuments.class);
			
			for(int i = 0; i < npsDocs.documents.size(); i++) {
				createJSONString(npsDocs.documents.get(i), i+1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createJSONString(NPSDocument doc, int articleNum) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(doc);
		createFile(json, articleNum);
	}
	
	private static void createFile(String json, int articleNum) {
		
		File directory = new File(Paths.get("").toAbsolutePath() + "/NPSArticles");
		directory.mkdir();
		PrintWriter writer;
		try {
			writer = new PrintWriter(Paths.get("").toAbsolutePath() + "/NPSArticles/article" + articleNum + ".json");
			writer.println(json);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}

class NPSDocuments {
	ArrayList<NPSDocument> documents;
}

class NPSDocument {
	String title;
	String body;
	String url;
}