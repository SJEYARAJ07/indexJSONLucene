# indexJSONLucene

    This repository contains java code to index JSON files created by a crawler using Lucene.

# Overview

    It also providess a functionality to search for a indexed item. 

# Dependencies

    - All code written in Java 13
    - All the Dependencies have been mentioned in the pom.xml

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

# Sample Output of Program:

    ************************************************************************
    6 Input files have been indexed: Total time taken: 821 ms
    ************************************************************************

    ************************************************************************
    10 documents found. Time taken for the search :144ms
    ************************************************************************
