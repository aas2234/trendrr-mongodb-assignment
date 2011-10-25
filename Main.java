import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/* Simple client for MongoD Player DB */
/* TODO: Add authentication parameters to call */

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			printWideReceiverForJets();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printWideReceiverForJets() {
		Mongo m = null;
		try {
			 m = new Mongo("dbh63.mongolab.com", 27637);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		DB trendrr = m.getDB("trendrr");
		
		/* add authentication line here */
		/* trendrr.authenticate("", "".toCharArray()); */
		
		DBCollection players = trendrr.getCollection("players");
		
		BasicDBObject query = new BasicDBObject();
		query.put("team", "jets");
		query.put("position", "wr");
		DBCursor cur = players.find(query);
		
		/* assuming jersey numbers start with zero */
		int maxJersey = 0;
		String maxJerseyPlayer = "";
		
		while(cur.hasNext()) {
			DBObject player = cur.next();
			String rawName = (String)player.get("name");
			String [] tokens = rawName.split(",");
			
			if (tokens.length != 2) {
				System.out.println("Invalid field in document for " + tokens[0]);
				continue;
			}
			try {
				int playerJersey = Integer.parseInt(tokens[1].trim());
				if (playerJersey > maxJersey) {
					maxJersey = playerJersey;
					maxJerseyPlayer = tokens[0];
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid field in document.");
				continue;
			}
		}
		System.out.println("Max Jersey Player in Jets : " + maxJerseyPlayer);
	}
}