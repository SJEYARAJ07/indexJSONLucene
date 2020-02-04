package org.jey.luceneindexcreator.Index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
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
import org.apache.lucene.util.Version;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.io.File;
//import static org.junit.Assert.assertEquals;
import java.nio.file.Files;

public class LuceneIndexWriter {
    
    IndexWriter indexWriter = null;
    private String inputJsonFilePath;
    private String outputLuceneIndexPath;

    //Constructor to initialize the file path
    public LuceneIndexWriter(String outputLuceneIndexPath, String inputJsonFilePath) {
        this.outputLuceneIndexPath = outputLuceneIndexPath;
        this.inputJsonFilePath = inputJsonFilePath;
    }

    //Create the Lucene Index
    public  void createLuceneIndex(){
        
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return arrayObjects;       
 
    }

    //Opening the Lucene Index and specify the IndexWriterConfig
    public boolean openLuceneIndex(){
        try {
            Directory dir = FSDirectory.open(new File(outputLuceneIndexPath));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_48, analyzer);

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
        for(JSONObject jsonList : (List<JSONObject>) jsonObjects){
            Document document = new Document();
            for(String field : (Set<String>) jsonList.keySet()){
                Class type = jsonList.get(field).getClass();
  
                if(type.equals(String.class))
                {
                	document.add(new StringField(field, (String)jsonList.get(field), Field.Store.NO));
                }
                else if(type.equals(Long.class))
                {
                	document.add(new LongField(field, (long)jsonList.get(field), Field.Store.YES));
                }
                else if(type.equals(Double.class))
                {
                	document.add(new DoubleField(field, (double)jsonList.get(field), Field.Store.YES));
                }
                else if(type.equals(Boolean.class))
                {
                	document.add(new StringField(field, jsonList.get(field).toString(), Field.Store.YES));
                }
            }
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

            //Check the index has been created successfully
            Directory indexDirectory = FSDirectory.open(new File(outputLuceneIndexPath));
            IndexReader indexReader = DirectoryReader.open(indexDirectory);

            // Search documents
            final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            long startTime = System.currentTimeMillis();
            
            //******* sample wildcard search ******* //
            
            //create a term to search file name
            Term term = new Term("description", "*principalmente Pokémon*");
            
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
    
    public static void main(String[] args)
    {
    	//Folder to create index files
    	String outputLuceneIndexPath = "/Users/jebaraj/IndexOutputJSON/";
    	//Folder containing input JSON files
        String inputJsonFilePath = "/Users/jebaraj/InputJSON/";
        
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