package com.springmvc.mongo.factory;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@SuppressWarnings("deprecation")
public class MongoFactory {
	private static Logger log = Logger.getLogger(MongoFactory.class);
	
	private static Mongo mongo;
	
	private MongoFactory() {}
	
	public static Mongo getMongo() {
		int portno = 27017;
		String hostname = "localhost";
		if(mongo == null) {
			try {
				mongo = new Mongo(hostname,portno);
			} catch (MongoException e) {
				log.error(e);
				// TODO: handle exception
			}
		}
		return mongo;
	}
	
	public static DB getDB(String db_name) {
		return getMongo().getDB(db_name);
	}
	
	public static DBCollection getCollection(String db_name, String db_collection) {
		return getDB(db_name).getCollection(db_collection);
	}
}
