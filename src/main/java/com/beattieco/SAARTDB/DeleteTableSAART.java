/** DeleteTableSAART
 * 	Java class to delete a DynamoDB NoSQL table of basic SAART information.
 *  Based on sample code from Amazon AWS.
 *	@author Matthew J. Beattie
 */

package com.beattieco.SAARTDB;

import java.util.Scanner;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DeleteTableSAART {

    public static void main(String[] args) throws Exception {

    	Scanner keyboard = new Scanner(System.in);
    	System.out.println("Enter name of table to delete:");
    	String deleteFileName = keyboard.next();
    	
    	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(deleteFileName);
        
        keyboard.close();

        try {
            System.out.println("Attempting to delete table; please wait...");
            table.delete();
            table.waitForDelete();
            System.out.print("Success.");

        }
        catch (Exception e) {
            System.err.println("Unable to delete table: ");
            System.err.println(e.getMessage());
        }
    }
}