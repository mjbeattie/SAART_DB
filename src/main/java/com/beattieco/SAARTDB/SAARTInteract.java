/** SAARTInteract
 * 	A class and set of methods to define specific actions for an Alexa skill.
 *	@author Matthew J. Beattie
 */

package com.beattieco.SAARTDB;

import java.util.HashMap;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.ScanFilter;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;

public class SAARTInteract {

	private Table table;
	
	/** SAARTInteract() constructor method loads a DynamoDB table into 
	 * the SAARTInteract instance in AWS DynamoDB
	 * @param tablename
	 */
	public SAARTInteract(String tablename) {
		//Load DynamoDB table into this.table using high level SDK client
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://dynamodb.us-east-1.amazonaws.com", "us-east-1"))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        this.table = dynamoDB.getTable(tablename);
	}  //end of SAARTInteract()
	
	
	/** scanByCompany() returns an item collection associated with a request for items
	 *  whose company field includes a String.
	 *  @param String compName:  the name of the company to search for
	 *  @return ItemCollection<ScanOutcome>
	*/
	public ItemCollection<ScanOutcome> scanByCompany(String compName) {
        ItemCollection<ScanOutcome> items = null;

        try {
        	//build scan criteria using company name
        	ScanFilter scanFilter = new ScanFilter("company").contains(compName);
        	items = table.scan(scanFilter);
        }
        catch (Exception e) {
            System.err.println("Unable to perform scan");
            System.err.println(e.getMessage());
        }
        return items;
	} //end of scanByCompany()
	
	
	/** queryBySVID() returns an item collection associated with a request for items
	 *  whose SVID is defined.  As a DynamoDB query, the match must be exact.
	 *  @param String SVID:  the SVID of the company to search for
	 *  @return ItemCollection<QueryOutcome>
	*/
	ItemCollection<QueryOutcome> queryBySVID(String svid) {
        ItemCollection<QueryOutcome> items = null;

        try {
        	//build query criteria using SVID.  Requires setting hash.
            HashMap<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("#svid", "svid");

            HashMap<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put(":ssssss", svid);

            QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#svid = :ssssss").withNameMap(nameMap)
                .withValueMap(valueMap);

        	items = table.query(querySpec);
        }
        catch (Exception e) {
            System.err.println("Unable to perform query");
            System.err.println(e.getMessage());
        }
        return items;
	} //end of queryBySVID()
	
	
	/** printScan() prints the result of a scan in pretty JSON format
	 *  @param scanResult
	 */
	void printScan(ItemCollection<ScanOutcome> scanResult) {
		Iterator<Item> iterator = null;
		Item item = null;
		
		try {
        	//iterate over results and print out in JSON format
            iterator = scanResult.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.toJSONPretty());
            }
		}
		catch (Exception e) {
			System.err.println("Unable to print scan");
			System.err.println(e.getMessage());
		}
		
	}  //end of printScan
	
	
	/** printQuery() prints the result of a query in pretty JSON format
	 *  @param scanResult
	 */
	void printQuery(ItemCollection<QueryOutcome> queryResult) {
		Iterator<Item> iterator = null;
		Item item = null;
		
		try {
        	//iterate over results and print out in JSON format
            iterator = queryResult.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.toJSONPretty());
            }
		}
		catch (Exception e) {
			System.err.println("Unable to print query");
			System.err.println(e.getMessage());
		}
		
	}  //end of printQuery
	
	
	/** scanCount() returns the number of items from a scan result.  Because a scan
	 *  uses ItemCollection, there is no method in that package to return the
	 *  number of elements.  They have to be counted with an iterator.
	 * @param scanResult
	 * @return
	 */
	int scanCount(ItemCollection<ScanOutcome> scanResult) {
		Iterator<Item> iterator = null;
		int count = 0;
		
		try {
        	//iterate over results and print out in JSON format
            iterator = scanResult.iterator();
            while (iterator.hasNext()) {
            	iterator.next();
            	count += 1;
            }
		}
		catch (Exception e) {
			System.err.println("Unable to count scan");
			System.err.println(e.getMessage());
		}
		
		return count;
	}  //end of scanCount

	
	/** queryCount() returns the number of items from a query result.  Because a scan
	 *  uses ItemCollection, there is no method in that package to return the
	 *  number of elements.  They have to be counted with an iterator.
	 * @param queryResult
	 * @return count
	 */
	int queryCount(ItemCollection<QueryOutcome> queryResult) {
		Iterator<Item> iterator = null;
		int count = 0;
		
		try {
        	//iterate over results and print out in JSON format
            iterator = queryResult.iterator();
            while (iterator.hasNext()) {
            	iterator.next();
            	count += 1;
            }
		}
		catch (Exception e) {
			System.err.println("Unable to count query");
			System.err.println(e.getMessage());
		}
		
		return count;
	}  //end of queryCount

	
	/** outputScan() saves the desired attribute from a scan to a String for readout
	 *  @param ItemCollection<ScanOutcome>scanResult, String attributeScanned, String attributeReturned
	 */
	public String outputScan(ItemCollection<ScanOutcome> scanResult, String attributeScanned,
			String attributeReturned) 
	{
		String readoutScan = "";
		Iterator<Item> iterator = null;
		Item item = null;
		int scanCount = scanCount(scanResult);
		
		try {
        	//iterate over results and save output to a String
			if (scanCount > 0) {
	            iterator = scanResult.iterator();
	            while (iterator.hasNext()) {
	                item = iterator.next();
	                readoutScan += "The " + attributeReturned + " of " + attributeScanned
	                		+ " " + item.getString(attributeScanned) + " is " 
	                		+ item.getString(attributeReturned) + "\n";
	            }
			}
			else {
				readoutScan += "The scan did not find any results.\n";
			}
		}
		catch (Exception e) {
			System.err.println("Unable to create output");
			System.err.println(e.getMessage());
		}
		return readoutScan;
	}  //end of printQuery
	
	
}
