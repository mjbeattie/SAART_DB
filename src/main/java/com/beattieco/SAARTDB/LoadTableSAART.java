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

            //Read in JSON file into object while converting all strings to
            //lower case.  DynamoDB cannot do case insensitive search.
            String svid = currentNode.path("SVID").asText();
            svid = svid.toLowerCase();
            String company = currentNode.path("COMPANY").asText();
            company = company.toLowerCase();
            String duns = currentNode.path("DUNS").asText();
            duns = duns.toLowerCase();
            String state = currentNode.path("STATE").asText();
            state = state.toLowerCase();
            String scvp = currentNode.path("SCVP").asText();
            scvp = scvp.toLowerCase();
            String sm = currentNode.path("SM").asText();
            sm = sm.toLowerCase();
            String cse = currentNode.path("CSE").asText();
            cse = cse.toLowerCase();
            String am = currentNode.path("AM").asText();
            am = am.toLowerCase();
            String asc = currentNode.path("ASC").asText();
            asc = asc.toLowerCase();
            String tsc = currentNode.path("TSC").asText();
            tsc = tsc.toLowerCase();
            String industry = currentNode.path("INDUSTRY").asText();
            industry = industry.toLowerCase();
            int employees = currentNode.path("EMPLOYEES").asInt();
            
            System.out.println(svid + " " + company);

            try {
            	Item item = new Item()
            			.withPrimaryKey("svid", svid, "company", company) 
            			.withString("duns", duns) 
            			.withString("state", state) 
            			.withString("scvp", scvp)  
            			.withString("sm", sm) 
            			.withString("cse", cse) 
            			.withString("am", am) 
            			.withString("asc", asc) 
            			.withString("tsc", tsc) 
            			.withString("industry", industry) 
            			.withNumber("employees", employees);

                table.putItem(item);
                System.out.println("PutItem succeeded: ");
                System.out.println(item.toJSONPretty());

            }
            catch (Exception e) {
                System.err.println("Unable to add SAART record: " + svid + " " + company);
                System.err.println(e.getMessage());
                break;
            }
        }
        parser.close();
    }
}