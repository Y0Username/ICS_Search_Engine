/**
 * Created by Yathish on 3/2/17.
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import com.mongodb.ServerAddress;
import java.util.Arrays;

public class MongoDump {

    public static void main( String args[] ) {

        try{
            // Open a connection to mongodb
//            MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017");
//            MongoClient client = new MongoClient(uri);
//            MongoOperations mongoOps = new MongoTemplate(client, "db_name");
//            mongoOps.findOne(new Query(Criteria.where("caseid").is(rawjsondata.getPatientid())),Patient.class,"patients");
//            mongoOps.insert(documentToBeAdded, "progressreports");
//            mongoOps.remove(new Query(Criteria.where("_id").is(documentToBeAdded.get_id())), "progressreports");


            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

            // Now connect to your databases
            DB db = mongoClient.getDB( "Movie" );
            DBCollection collection = db.getCollection("movies");
//            collection.insert({"name":"tutorials point"});
            System.out.println("Connect to database successfully");
            //boolean auth = db.authenticate(myUserName, myPassword);
            //System.out.println("Authentication: "+auth);

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}

