/**
 * 
 */
package org.bgu.ise.ddb.items;

import java.io.IOException;


import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import org.bgu.ise.ddb.MediaItems;
import org.bgu.ise.ddb.ParentController;
import org.bgu.ise.ddb.User;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Alex
 *
 */
@RestController
@RequestMapping(value = "/items")
public class ItemsController extends ParentController {
	/**
	 * The function copy all the items(title and production year) from the Oracle table MediaItems to the System storage.
	 * The Oracle table and data should be used from the previous assignment
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "fill_media_items", method={RequestMethod.GET})
	public void fillMediaItems(HttpServletResponse response) throws ClassNotFoundException, SQLException{
		System.out.println("was here");
		//:TODO your implementation
		Connection conn = null;
        PreparedStatement ps = null;
        String server_name = "132.72.64.124";
        String userName = "liella";
        String pass = ">|p?sZEJ";
        String url = "jdbc:sqlserver://" + server_name + ":1433;databaseName=" + userName + ";user=" + userName + ";" + "password=" + pass + ";encrypt=false;";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection(url, userName, pass);
        String query = "SELECT TITLE , PROD_YEAR FROM MediaItems";
        ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("MediaItems");
        while (rs.next()) {
            String titel = rs.getString(2);
            String year = rs.getString(1);
            Document doc = new Document();
            doc.put("TITLE", titel);
            doc.put("PROD_YEAR", year);
            collection.insertOne(doc);
        }
        ps.close();
        rs.close();
        mongoClient.close();
		HttpStatus status = HttpStatus.OK;
		response.setStatus(status.value());
	}
	
	

	/**
	 * The function copy all the items from the remote file,
	 * the remote file have the same structure as the films file from the previous assignment.
	 * You can assume that the address protocol is http
	 * @throws IOException 
	 */
	@RequestMapping(value = "fill_media_items_from_url", method={RequestMethod.GET})
	public void fillMediaItemsFromUrl(@RequestParam("url")    String urladdress,
			HttpServletResponse response) throws IOException{
		URL url = new URL(urladdress);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);
        MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("MediaItems");
        for (CSVRecord record : csvParser) {
            // Perform your operations with the record data
            Document doc = new Document();
            doc.put("TITLE", record.get(1));
            doc.put("PROD_YEAR", record.get(0));
            collection.insertOne(doc);
        }
        csvParser.close();
        reader.close();
        inputStream.close();
        mongoClient.close();
		HttpStatus status = HttpStatus.OK;
		response.setStatus(status.value());
	}
	
	
	/**
	 * The function retrieves from the system storage N items,
	 * order is not important( any N items) 
	 * @param topN - how many items to retrieve
	 * @return
	 */
	@RequestMapping(value = "get_topn_items",headers="Accept=*/*", method={RequestMethod.GET},produces="application/json")
	@ResponseBody
	@org.codehaus.jackson.map.annotate.JsonView(MediaItems.class)
	public  MediaItems[] getTopNItems(@RequestParam("topn")    int topN){
		//:TODO your implementation
		MediaItems[] items = new MediaItems[topN];
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("MediaItems");
		// Retrieving the documents
		FindIterable<Document> iterDoc = collection.find();
		Iterator<Document> it = iterDoc.iterator();
		do {
			if (iterDoc != null) {
				Document doc = (Document) it.next();
				String title = doc.getString("TITLE");
				Integer year = Integer.parseInt(doc.getString("PROD_YEAR"));
				MediaItems item = new MediaItems(title, year);
				items[topN-1] = item;
				topN = topN - 1;
			}
		} while (it.hasNext() && topN>0);
		mongoClient.close();
		return items;
	}
		

}
