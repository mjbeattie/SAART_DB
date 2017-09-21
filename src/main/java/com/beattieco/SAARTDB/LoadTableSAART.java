/** LoadTableSAART
 * 	Java class to load a JSON file into a DynamoDB NoSQL table of 
 *  basic SAART information.
 *  Based on sample code from Amazon AWS.
 *	@author Matthew J. Beattie
 */
package com.beattieco.SAARTDB;

import java.io.File;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LoadTableSAART {

    public static void main(String[] args) throws Exception {

        final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://dynamodb.us-east-1.amazonaws.com", "us-east-1"))
            .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Companies");

        JsonParser parser = new JsonFactory().createParser(new File("SAART_Extract.json"));

        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();

        ObjectNode currentNode;

        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();

            String SVID = currentNode.path("SVID").asText();
            String company = currentNode.path("COMPANY").asText();
            String DUNS = currentNode.path("DUNS").asText();
            String state = currentNode.path("STATE").asText();
            String SCVP = currentNode.path("SCVP").asText();
            String SM = currentNode.path("SM").asText();
            String CSE = currentNode.path("CSE").asText();
            String AM = currentNode.path("AM").asText();
            String ASC = currentNode.path("ASC").asText();
            String TSC = currentNode.path("TSC").asText();
            String industry = currentNode.path("INDUSTRY").asText();
            int employees = currentNode.path("EMPLOYEES").asInt();
            
            System.out.println(SVID + " " + company);

            try {
            	Item item = new Item()
            			.withPrimaryKey("SVID", SVID, "company", company) 
            			.withString("DUNS", DUNS) 
            			.withString("state", state) 
            			.withString("SCVP", SCVP)  
            			.withString("SM", SM) 
            			.withString("CSE", CSE) 
            			.withString("AM", AM) 
            			.withString("ASC", ASC) 
            			.withString("TSC", TSC) 
            			.withString("industry", industry) 
            			.withNumber("employees", employees);

                table.putItem(item);
                System.out.println("PutItem succeeded: ");
                System.out.println(item.toJSONPretty());

            }
            catch (Exception e) {
                System.err.println("Unable to add movie: " + SVID + " " + company);
                System.err.println(e.getMessage());
                break;
            }
        }
        parser.close();
    }
}