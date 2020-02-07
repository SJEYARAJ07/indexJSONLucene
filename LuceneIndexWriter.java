package org.jey.luceneindexcreator.Index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path; 

public class LuceneIndexWriter {
    
    private static final String String = null;
	IndexWriter indexWriter = null;
    private String inputJsonFilePath;
    private static String outputLuceneIndexPath;

    //Constructor to initialize the file path
    public LuceneIndexWriter(String outputLuceneIndexPath, String inputJsonFilePath) {
        this.outputLuceneIndexPath = outputLuceneIndexPath;
        this.inputJsonFilePath = inputJsonFilePath;
    }

    //Create the Lucene Index
    public  void createLuceneIndex() throws IOException {
        
    	JSONArray jsonObjects = parseInputJsonFile();
        
        openLuceneIndex();
        addLuceneDocuments(jsonObjects);

        wrapupAndFinish();       
 
    }

//Parse the input JSON file 
    public JSONArray parseInputJsonFile(){

        JSONParser parser = new JSONParser();
        JSONArray arrayObjects = null;
		try {
			arrayObjects = (JSONArray) parser.parse(new FileReader(inputJsonFilePath));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

        return arrayObjects;       
 
    }

    //Opening the Lucene Index and specify the IndexWriterConfig
    public boolean openLuceneIndex(){
        try {
        
            Path indexPath = Paths.get(outputLuceneIndexPath);
        	Directory dir = FSDirectory.open(indexPath);
            Analyzer analyzer = new StandardAnalyzer();
            
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            
            System.out.println(indexPath);

            //IndexWriterConfig allows you specify the open mode:
            //Available 3 options:
            //* IndexWriterConfig.OpenMode.APPEND
            //* IndexWriterConfig.OpenMode.CREATE
            //* IndexWriterConfig.OpenMode.CREATE_OR_APPEND
            indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(dir, indexWriterConfig);

            return true;

        } catch (Exception e) {
            System.err.println("Error - Opening the index. " + e.getMessage());

        }
        return false;

    }

//Add to Lucene documents
	public void addLuceneDocuments(JSONArray jsonObjects){
    		

   	    FieldType textIndexedTypeTrue = new FieldType();
	    textIndexedTypeTrue.setStored(true);
	    textIndexedTypeTrue.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
  
        for (Object O: jsonObjects) {
   
            Document document = new Document();

        	JSONObject news = (JSONObject) O;

    		String section = "";
    	    if (news.get("section") != null) {
    	    	section = (String)news.get("section");    	   
    	    }

    	    String title = "";
    	    if (news.get("title") != null) {
    	    	title = (String)news.get("title");    	   
    	    }

    	    String description = "";
    	    if (news.get("description") != null) {
    	    	description = (String)news.get("description");    	   
    	    }
    	    
    	    String name = "";
    	    if (news.get("name") != null) {
    	    	name = (String)news.get("name");    	   
    	    }
    	    
    	    String created = "";
    	    if (news.get("created") != null) {
    	    	created = (String)news.get("created");    	   
    	    }
    	    
    	    String imagelink = "";
    	    if (news.get("image-link") != null) {
    	    	imagelink = (String)news.get("image-link");    	   
    	    }

//    	    String images = "";
//    	    if (news.get("images") != null) {
//    	    	images = (String)news.get("images");    	   
//    	    }

    	    String loc = "";
    	    if (news.get("loc") != null) {
    	    	loc = (String)news.get("loc");    	   
    	    }
    	    
    	    String text = "";
    	    if (news.get("text") != null) {
    	    	text = (String)news.get("text");    	   
    	    }
    	    
    	    String quotedText = "";
    	    if (news.get("quotedText") != null) {
    	    	quotedText = (String)news.get("quotedText");    	   
    	    }
    	    
    	    document.add(new StringField("section",section,Field.Store.YES));
    	    document.add(new StringField("title",title,Field.Store.YES));	    
    	    document.add(new StringField("description",description,Field.Store.YES));
    	    document.add(new StringField("name",name,Field.Store.YES));
//    	    document.add(new StringNoIndexedField("name",name));
    	      	    
    	    document.add(new StringField("created",created,Field.Store.YES));
    	    document.add(new StringNoIndexedField("imagelink",imagelink));
//    	    document.add(new StringNoIndexedField("images",images));
    	    
    	    document.add(new StringField("loc",section,Field.Store.YES));
    	    document.add(new StringField("text",title,Field.Store.YES));	    
    	    document.add(new StringField("quotedText",description,Field.Store.YES));
    	    
            
    	    try {
                indexWriter.addDocument(document);
            } catch (IOException ex) {
                System.err.println("Error adding documents to the index. " +  ex.getMessage());
            }
    	}

    }

    public void wrapupAndFinish(){
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }
 
    public static void cleanupIndexFolder(File outputLuceneIndexFolder)
    {
    	final File[] indexFolder = outputLuceneIndexFolder.listFiles();
        for (File f: indexFolder) f.delete(); 
    }
    
    public static void wildcardSearchOnIndex(String outputLuceneIndexPath)
    {
    	try 
    	{

            Path indexPath = Paths.get(outputLuceneIndexPath);
        	Directory dir = FSDirectory.open(indexPath);
            IndexReader indexReader = DirectoryReader.open(dir);

            // Search documents
            final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            long startTime = System.currentTimeMillis();
            
   //         testWriteIndex();
            
            //******* sample wildcard search ******* //
            
            //create a term to search file name
            Term term = new Term("description", "*Animal Lover/Animals *");
            
            //******* sample wildcard search ******* //
            
            //create the term query object
            Query query = new WildcardQuery(term);

    		System.out.println("\nSearching for '" + query + "' using WildcardQuery");
    		
    		//Perform the search
            TopDocs topDocs = indexSearcher.search(query,10);
            long endTime = System.currentTimeMillis();
            
            System.out.println("");
            System.out.println("************************************************************************");
            System.out.println(topDocs.totalHits + " documents found. Time taken for the search :" + (endTime - startTime) + "ms");
            System.out.println("************************************************************************");
            System.out.println("");
            
        } 
    	catch (Exception e) {
            e.printStackTrace();
        }    	
    }
  
    
    
    
    
    public static void main(String[] args) throws IOException
    {
    	//Folder to create index files
    	String outputLuceneIndexPath = "/Users/jebaraj/IndexOutputJSON/";
    	//Folder containing input JSON files
        String inputJsonFilePath = "/Users/jebaraj/InputJSON1/";
        
        //Index folder Clean up before commencing the process
        File outputLuceneIndexFolder = new File(outputLuceneIndexPath);
        cleanupIndexFolder(outputLuceneIndexFolder);
       
        File[] files = new File(inputJsonFilePath).listFiles();

    	//Start time for Indexing
        long startTime = System.currentTimeMillis(); 
        
        int Filescount = new File(inputJsonFilePath).listFiles().length;
        System.out.println(Filescount);
        
        for (File file : files) 
        { 
          if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() )
          { 
        	System.out.println(file.getAbsolutePath()); 
        	String filePath = file.getAbsolutePath();
            LuceneIndexWriter lw = new LuceneIndexWriter(outputLuceneIndexPath,filePath);
            lw.createLuceneIndex();
           
          }
        } 

        
        //End time of Index creation        
        long endTime = System.currentTimeMillis(); 
 
        System.out.println("");
        System.out.println("************************************************************************");
        //Total time taken to index the JSON files
        System.out.println(Filescount + " Input files have been indexed: Total time taken: " +(endTime-startTime)+" ms"); 
        System.out.println("************************************************************************");
        System.out.println("");

        //Wild card search on a single JSON element
        wildcardSearchOnIndex(outputLuceneIndexPath);

    }

}
