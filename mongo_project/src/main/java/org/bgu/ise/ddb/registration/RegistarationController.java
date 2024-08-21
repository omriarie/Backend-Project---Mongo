/**
 * 
 */

package org.bgu.ise.ddb.registration;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.bgu.ise.ddb.ParentController;
import org.bgu.ise.ddb.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.bgu.ise.ddb.ParentController;
import org.bgu.ise.ddb.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.bson.Document;




/**
 * @author Alex
 *
 */

@RestController
@RequestMapping(value = "/registration")
public class RegistarationController extends ParentController{
//	MongoClient mongoClient = (MongoClient) MongoClients.create("mongodb+srv://localhost/");
//	MongoDatabase db = mongoClient.getDatabase("BigDataProjectDB");
	/**
	 * The function checks if the username exist,
	 * in case of positive answer HttpStatus in HttpServletResponse should be set to HttpStatus.CONFLICT,
	 * else insert the user to the system  and set to HttpStatus in HttpServletResponse HttpStatus.OK
	 * @param usernamess
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param response
	 */
	@RequestMapping(value = "register_new_customer", method={RequestMethod.POST})
	public void registerNewUser(@RequestParam("username") String username,
			@RequestParam("password")    String password,
			@RequestParam("firstName")   String firstName,
			@RequestParam("lastName")  String lastName,
			HttpServletResponse response){
		System.out.println(username+" "+password+" "+lastName+" "+firstName);
		//:TODO your implementation
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		try {
			if (this.isExistUser(username)) {
				HttpStatus status = HttpStatus.CONFLICT;
				response.setStatus(status.value());
			} else {
				MongoCollection<Document> collection = db.getCollection("Users");
				Document document = new Document();
				document.put("username", username);
				document.put("password", password);
				document.put("firstName", firstName);
				document.put("lastName", lastName);
				LocalDate dateObj = LocalDate.now();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        String date = dateObj.format(formatter);
				document.put("date", date);
				// Inserting the document into the collection
				collection.insertOne(document);
				HttpStatus status = HttpStatus.OK;
				response.setStatus(status.value());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mongoClient.close();
		
	}
	/**
	 * The function returns true if the received username exist in the system otherwise false
	 * @param username
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "is_exist_user", method = { RequestMethod.GET })
	public boolean isExistUser(@RequestParam("username") String username) throws IOException{
		System.out.println(username);
		boolean result = false;
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("Users");
		Document doc = collection.find(eq("username", username)).first();
		if (doc != null) {
			result = true;
		}
		return result;
	}
	
	/**
	 * The function returns true if the received username and password match a system storage entry, otherwise false
	 * @param username
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "validate_user", method={RequestMethod.POST})
	public boolean validateUser(@RequestParam("username") String username,
			@RequestParam("password")    String password) throws IOException{
		System.out.println(username+" "+password);
		boolean result = false;
		//:TODO your implementation
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("Users");
		Document doc = collection.find(eq("username", username)).first();
		if(doc != null && doc.get("password").equals(password)) {
			result = true;
		}
		mongoClient.close();
		return result;
		
	}
	
	/**
	 * The function retrieves number of the registered users in the past n days
	 * @param days
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "get_number_of_registred_users", method={RequestMethod.GET})
	public int getNumberOfRegistredUsers(@RequestParam("days") int days) throws IOException{
		System.out.println(days+"");
		int result = 0;
		//:TODO your implementation
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowdate = dateObj.format(formatter);
        LocalDate datenow = LocalDate.parse(nowdate);
        String date = dateObj.format(formatter);
        LocalDate date1 = LocalDate.parse(date);
        
        MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("Users");
		
		User[] allUsers = this.getAllUsers();
		for (User user : allUsers) {
			String dateuser = user.getDate();
			LocalDate date2 = LocalDate.parse(dateuser);
			int temp = (int) ChronoUnit.DAYS.between(date1, date2);
			if (ChronoUnit.DAYS.between(date2,date1) < days) {
				result++;
			}
		}
        mongoClient.close();
		return result;
		
	}
	
	/**
	 * The function retrieves all the users
	 * 
	 * @return
	 */
	@RequestMapping(value = "get_all_users", headers = "Accept=*/*", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	@org.codehaus.jackson.map.annotate.JsonView(User.class)
	public User[] getAllUsers() {
		List<User> u = new ArrayList<>();
//		Creating a collection object
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("BigDataProject");
		MongoCollection<Document> collection = db.getCollection("Users");
		// Retrieving the documents
		FindIterable<Document> iterDoc = collection.find();
		Iterator<Document> it = iterDoc.iterator();
		do {
			if (iterDoc != null) {
				Document doc = (Document) it.next();
				User newUser = new User(doc.getString("username"), doc.getString("password"),doc.getString("firstName"), doc.getString("lastName"), doc.getString("date"));
				u.add(newUser);
			}
		} while (it.hasNext());
		System.out.println(u);
		mongoClient.close();
		return u.toArray(new User[0]);
	}
	
	

}
