/** CreateTableSAART
 * 	Java class to create a DynamoDB NoSQL table of basic SAART information.
 *  Based on sample code from Amazon AWS.
 *	@author Matthew J. Beattie
 */
package com.beattieco.SAARTDB;

import java.util.Arrays;

//Import AWS JDK libraries
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


public class CreateTableSAART {

    public static void main(String[] args) throws Exception {

    	//This block creates a client builder, that accesses AWS
    	//Note:  to access local DynamoDB test enviroment, Endpoint is "http://localhost:8000", "us-west-2"
    	//       for AWS DynamoDB live, endpoint is "http://dynamodb.us-east-1.amazonaws.com", "us-east-1"
    	System.out.println("Creating client builder...");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://dynamodb.us-east-1.amazonaws.com", "us-east-1"))
            .build();

        //Client builder is used to create a client
        System.out.println("Creating new client dynamoDB...");
        DynamoDB dynamoDB = new DynamoDB(client);

        String tableName = "Companies";

        //Create an empty NoSQL database with year and title attributes
        try {
            System.out.println("Attempting to create table; please wait...");
    
        	Table table = dynamoDB.createTable(tableName,
        			//Key fields
        			Arrays.asList(
        					new KeySchemaElement("svid", KeyType.HASH), 
        					new KeySchemaElement("company", KeyType.RANGE)
        					),
        			//Attribute fields
        			Arrays.asList(
        					new AttributeDefinition("svid", ScalarAttributeType.S),
        					new AttributeDefinition("company", ScalarAttributeType.S)),
       /* 					new AttributeDefinition("duns", ScalarAttributeType.S),
        					new AttributeDefinition("state", ScalarAttributeType.S),
        					new AttributeDefinition("scvp", ScalarAttributeType.S),
        					new AttributeDefinition("sm", ScalarAttributeType.S),
        					new AttributeDefinition("cse", ScalarAttributeType.S),
        					new AttributeDefinition("am", ScalarAttributeType.S),
        					new AttributeDefinition("asc", ScalarAttributeType.S),
        					new AttributeDefinition("tsc", ScalarAttributeType.S),
        					new AttributeDefinition("industry", ScalarAttributeType.S),
        					new AttributeDefinition("employees", ScalarAttributeType.N)
        					),
       */
            new ProvisionedThroughput(10L, 10L));
    
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

        }
        catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
     
    }
}