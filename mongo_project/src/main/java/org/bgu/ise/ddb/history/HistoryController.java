/**
 * 
 */
package org.bgu.ise.ddb.history;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.bson.Document;
import javax.servlet.http.HttpServletResponse;

import org.bgu.ise.ddb.MediaItems;
import org.bgu.ise.ddb.ParentController;
import org.bgu.ise.ddb.User;
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

/**
 * @author Alex
 *
 */
@RestController
@RequestMapping(value = "/history")
public class HistoryController extends ParentController{
	
	/**
	 * The function inserts to the system storage triple(s)(username, title, timestamp). 
	 * The timestamp - in ms since 1970
	 * Advice: better to insert the history into two structures( tables) in 
	 * order to extract it fast one with the key - username, another with the key - title
	 * @param username
	 * @param title
	 * @param response
	 */
	@RequestMapping(value = "insert_to_history", method={RequestMethod.GET})
	public void insertToHistory (@RequestParam("username")    String username,
			@RequestParam("title")   String title,
			HttpServletResponse response){
		System.out.println(username+" "+title);
		//:TODO your implementation
		Long timestamp = System.currentTimeMillis();
		Date datems = new Date(timestamp);
		Document doc1 = new Document();
		doc1.append("username", username);
		doc1.append("title", title);
		doc1.append("timestamp", datems);
		Document doc2 = new Document();
		doc2.append("username", username);
		doc2.append("title", title);
		doc2.append("timestamp", datems);
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		db.getCollection("Items_History").insertOne(doc1);
		db.getCollection("Users_History").insertOne(doc2);
		HttpStatus status = HttpStatus.OK;
		response.setStatus(status.value());
	}
	
	
	
	/**
	 * The function retrieves  users' history
	 * The function return array of pairs <title,viewtime> sorted by VIEWTIME in descending order
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "get_history_by_users",headers="Accept=*/*", method={RequestMethod.GET},produces="application/json")
	@ResponseBody
	@org.codehaus.jackson.map.annotate.JsonView(HistoryPair.class)
	public  HistoryPair[] getHistoryByUser(@RequestParam("entity")    String username){
		//:TODO your implementation
		List<HistoryPair> pairs = new ArrayList<>();
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("Users_History");
		// Retrieving the documents
		FindIterable<Document> iterDoc = collection.find();
		Iterator<Document> it = iterDoc.iterator();
		do {
			if (iterDoc != null) {
				Document doc = (Document) it.next();
				if(doc.getString("username").equals(username)) {
					HistoryPair pair = new HistoryPair(doc.getString("title"),doc.getDate("timestamp"));
					pairs.add(pair);
				}
			}
		} while (it.hasNext());
        Collections.sort(pairs, Comparator.comparing(HistoryPair::getViewtime).reversed());
        HistoryPair[] finel_pairs = new HistoryPair[pairs.size()];
        int i = 0;
        for(HistoryPair temp : pairs) {
        	HistoryPair hp = new HistoryPair(temp.getCredentials(),temp.getViewtime());
        	finel_pairs[i] = hp;
        	i= i + 1;
        }
		return finel_pairs;
	}
	
	
	/**
	 * The function retrieves  items' history
	 * The function return array of pairs <username,viewtime> sorted by VIEWTIME in descending order
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "get_history_by_items",headers="Accept=*/*", method={RequestMethod.GET},produces="application/json")
	@ResponseBody
	@org.codehaus.jackson.map.annotate.JsonView(HistoryPair.class)
	public  HistoryPair[] getHistoryByItems(@RequestParam("entity")    String title){

		List<HistoryPair> pairs = new ArrayList<>();
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("Items_History");
		// Retrieving the documents
		FindIterable<Document> iterDoc = collection.find();
		Iterator<Document> it = iterDoc.iterator();
		do {
			if (iterDoc != null) {
				Document doc = (Document) it.next();
				if(doc.getString("title").equals(title)) {
					HistoryPair pair = new HistoryPair(doc.getString("username"),doc.getDate("timestamp"));
					pairs.add(pair);
				}
			}
		} while (it.hasNext());
        Collections.sort(pairs, Comparator.comparing(HistoryPair::getViewtime).reversed());
        HistoryPair[] finel_pairs = new HistoryPair[pairs.size()];
        int i = 0;
        for(HistoryPair temp : pairs) {
        	HistoryPair hp = new HistoryPair(temp.getCredentials(),temp.getViewtime());
        	finel_pairs[i] = hp;
        	i= i + 1;
        }
		return finel_pairs;
	}
	
	/**
	 * The function retrieves all the  users that have viewed the given item
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "get_users_by_item",headers="Accept=*/*", method={RequestMethod.GET},produces="application/json")
	@ResponseBody
	@org.codehaus.jackson.map.annotate.JsonView(HistoryPair.class)
	public  User[] getUsersByItem(@RequestParam("title") String title){
		//:TODO your implementation7
		List<User> user = new ArrayList<>();
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> Items_collection = db.getCollection("Items_History");
		MongoCollection<Document> Users_collection = db.getCollection("Users");
		// Retrieving the documents
		FindIterable<Document> iterDoc = Items_collection.find();
		Iterator<Document> it = iterDoc.iterator();
		do {
			if (iterDoc != null) {
				Document doc = (Document) it.next();
				if(doc.getString("title").equals(title)) {
					Document doc2 = Users_collection.find(eq("username", doc.getString("username"))).first();
					User newUser = new User(doc2.getString("username"), doc2.getString("password"),doc2.getString("firstName"), doc2.getString("lastName"), doc2.getString("date"));
					user.add(newUser);
				}
			}
		} while (it.hasNext());
		User[] finel_users = new User[user.size()];
        int i = 0;
        for(User temp : user) {
        	finel_users[i] = temp;
        	i= i + 1;
        }
		return finel_users;
	}
	
	/**
	 * The function calculates the similarity score using Jaccard similarity function:
	 *  sim(i,j) = |U(i) intersection U(j)|/|U(i) union U(j)|,
	 *  where U(i) is the set of usernames which exist in the history of the item i.
	 * @param title1
	 * @param title2
	 * @return
	 */
	@RequestMapping(value = "get_items_similarity",headers="Accept=*/*", method={RequestMethod.GET},produces="application/json")
	@ResponseBody
	public double  getItemsSimilarity(@RequestParam("title1") String title1,
			@RequestParam("title2") String title2){
		double intersection = 0;
		User[] users1 = this.getUsersByItem(title1);
		User[] users2 = this.getUsersByItem(title2);
		double union = users1.length + users2.length;
		//calculate intersections
		for (User user1 : users1) {
			for (User user2 : users2) {
				if(user1.getUsername().equals(user2.getUsername())){
					intersection += 1;
					union -= 1;
				}
			}
		}
		
		double ret = (intersection / union);
		return ret;
	}
	

}
