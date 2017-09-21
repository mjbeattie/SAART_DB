package com.beattieco.SAARTDB;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;

public class TestSAART {

	public static void main(String[] args) throws Exception {
    	SAARTInteract beattieTable = new SAARTInteract("Companies");
    	
    	//Test scan functionality
    	System.out.println("\n\n********** TESTING SCAN *********");
    	ItemCollection<ScanOutcome> scanResult = beattieTable.scanByCompany("ELECTRO");
    	
    	int scanCount = beattieTable.scanCount(scanResult);
    	if (scanCount > 0) {
    		System.out.println("Number of items returned is " + beattieTable.scanCount(scanResult));
        	beattieTable.printScan(scanResult);
    	}
    	else {
    		System.out.println("Scan returned zero results.");
    	}
    	
    	System.out.println("\n\n********** TESTING OUTPUT *********");
    	String output = beattieTable.outputScan(beattieTable.scanByCompany("ELECTRO"), "company", "CSE");
    	System.out.println(output);
    	
    	//Test query functionality
    	System.out.println("\n\n********** TESTING QUERY *********");
    	ItemCollection<QueryOutcome> queryResult = beattieTable.queryBySVID("000Z0O");
    	
    	int queryCount = beattieTable.queryCount(queryResult);
    	if (queryCount > 0) {
    		System.out.println("Number of items returned is " + beattieTable.queryCount(queryResult));
        	beattieTable.printQuery(queryResult);
    	}
    	else {
    		System.out.println("Query returned zero results.");
    	}
    }  //end of main()
	
	
}
