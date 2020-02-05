# indexJSONLucene

    This repository contains java code to index JSON files created by a crawler using Lucene.

# Overview

    Crawler for gun violence generates the JSON files as inputs to this program. indexJSONLuncene package iterates thru each JSON file and creates the index. Functionality has been provided to do a wild card search query to validate if the index has been created using Lucene's queryparser. 
    
# Dependencies

    - All code written in Java 13
    - All the Dependencies have been mentioned in the pom.xml
    
    ```
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
          <modelVersion>4.0.0</modelVersion>

          <groupId>ReadfromJson</groupId>
          <artifactId>ReadfromJson</artifactId>
          <version>0.0.1-SNAPSHOT</version>

            <dependencies>
                <dependency>
                    <groupId>org.apache.lucene</groupId>
                    <artifactId>lucene-core</artifactId>
                    <version>4.8.0</version>
                </dependency>
                <dependency>
                    <groupId>junit</groupId>
                    <artifactId>junit</artifactId>
                    <version>4.11</version>
                    <scope>test</scope>
                </dependency>

                <dependency>
                    <groupId>org.apache.lucene</groupId>
                    <artifactId>lucene-analyzers-common</artifactId>
                    <version>4.8.0</version>
                </dependency>
                <dependency>
                    <groupId>org.apache.lucene</groupId>
                    <artifactId>lucene-queryparser</artifactId>
                    <version>4.8.0</version>
                </dependency>
                <dependency>
                    <groupId>org.apache.lucene</groupId>
                    <artifactId>lucene-highlighter</artifactId>
                    <version>4.8.0</version>
                </dependency>



                <!-- json Parser -->
                <dependency>
                    <groupId>com.googlecode.json-simple</groupId>
                    <artifactId>json-simple</artifactId>
                    <version>1.1.1</version>
                </dependency>



            </dependencies>
          <build>
            <sourceDirectory>src</sourceDirectory>
            <plugins>
              <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                  <release>13</release>
                </configuration>
              </plugin>
            </plugins>
          </build>
        </project>
    ```

# How it all fits together

1) Inputs to the program:

    Place the JSON files created by crawler under inputJsonFilePath ("/Users/jebaraj/InputJSON/")
    Program will create inndexed files in the folder you specify (outputLuceneIndexPath --> /Users/jebaraj/IndexOutputJSON/) 

2) Outlined below is the flow of the program.

    - LuceneIndexWriter
        - cleanupIndexFolder
        - createLuceneIndex
            - parseInputJsonFile
            - openLuceneIndex
            - addLuceneDocuments
            - wrapupAndFinish
      - wildcardSearchOnIndex

3)  Query Parser - Sample text on description key of JSON
    
    ```
     //create a term to search file name
            Term term = new Term("description", "*principalmente Pokémon*");
    ```
    
# Sample Output of Program:

    ************************************************************************
    6 Input files have been indexed: Total time taken: 821 ms
    ************************************************************************

    ************************************************************************
    10 documents found. Time taken for the search :144ms
    ************************************************************************
