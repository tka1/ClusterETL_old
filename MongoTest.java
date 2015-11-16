


import com.mongodb.*;
public class MongoTest {

	   public static void main(String[] args) {
	        try {
	            // connect to mongo server
	            MongoClient mongo = new MongoClient("localhost", 27017);

	            // create a blank database
	            DB db = mongo.getDB("kode12");
	            
	            DBCollection coll = db.getCollection("mycol");
	            System.out.println("Collection created successfully");
	            
	            BasicDBObject doc = new BasicDBObject("title", "MongoDB").
	                    append("description", "database").
	                    append("likes", 100).
	                    append("url", "http://www.tutorialspoint.com/mongodb/").
	                    append("by", "tutorials point");
	        				
	                 coll.insert(doc);
	                 System.out.println("Document inserted successfully");



	            //display statistics
	            //System.out.println(db.getStats());
	        } catch (Exception e) {
	            System.out.println("Error in creating database.");
	            e.printStackTrace();
	        }
	    }

}
