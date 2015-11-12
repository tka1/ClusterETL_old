package clusteretl;
import java.net.UnknownHostException;

import com.mongodb.*;
public class MongoTest {

	   public static void main(String[] args) {
	        try {
	            // connect to mongo server
	            MongoClient mongo = new MongoClient("localhost", 27017);

	            // create a blank database
	            DB db = mongo.getDB("kode12");

	            //display statistics
	            System.out.println(db.getStats());
	        } catch (UnknownHostException e) {
	            System.out.println("Error in creating database.");
	            e.printStackTrace();
	        }
	    }

}
