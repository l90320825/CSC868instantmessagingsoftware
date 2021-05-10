import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
/**
 *  Alvaro Maroto
 */
MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

MongoDatabase database = mongoClient.getDatabase("InstantMessagingDB");

MongoCollection<Document> userscollection = database.getCollection("users");
/**
Document userdoc = new Document("username", "test");
 */
MongoCollection<Document> chatroommessagecollection = database.getCollection("chatroommessage");
/**
Document crdoc = new Document("message", "test message")
                .append("sender", "test")
                .append("chatroomname", "chatroom test")
                .append("time", null);
 */
MongoCollection<Document> directmessagecollection = database.getCollection("directmessage");
/**
Document crdoc = new Document("message", "test message")
                .append("sender", "test")
                .append("reciever", "test2")
                .append("time", null);
 */
