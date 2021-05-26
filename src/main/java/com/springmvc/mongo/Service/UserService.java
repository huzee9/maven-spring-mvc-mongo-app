package com.springmvc.mongo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.springmvc.mongo.Bean.User;
import com.springmvc.mongo.factory.MongoFactory;

@Service("userService")
@Transactional
public class UserService {
	static String db_name = "myspringmvcdb", db_collection = "mycollection";
	private static Logger log = Logger.getLogger(UserService.class);
	
	public List getAll() {
		List user_list = new ArrayList();
		DBCollection coll = MongoFactory.getCollection(db_name, db_collection);
		
		DBCursor cursor = coll.find();
		while(cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			
			User user = new User();
			user.setId(dbObject.get("id").toString());
			user.setName(dbObject.get("name").toString());
			
			user_list.add(user);
		}
		log.debug("Total records fetched from the mongo database are= " + user_list.size());
		return user_list;
	}
	
	public Boolean addUser(User user) {
		boolean output = false;
		Random ran = new Random();
		log.debug("Adding new user to mongo DB; Entered username is= "+ user.getName());
		
		try {
			DBCollection coll = MongoFactory.getCollection(db_name, db_collection);
			BasicDBObject doc = new BasicDBObject();
			doc.put("id", String.valueOf(ran.nextInt(100)));
			doc.put("name", user.getName());
			
			coll.insert(doc);
			output = true;
		} catch (Exception e) {
			output = false;
			log.error("An error occurred while saving a new user to the mongo db", e);
			// TODO: handle exception
		}
		
		return output;
	}
	
	public Boolean editUser(User user) {
		boolean output = false;
		log.debug("Updating existing user; Entered user id is=" + user.getId());
		try {
			BasicDBObject existing = (BasicDBObject)getDBObject(user.getId());
			DBCollection coll = MongoFactory.getCollection(db_name, db_collection);
			
			BasicDBObject edited = new BasicDBObject();
			edited.put("id", user.getId());
			edited.put("name", user.getName());
			
			coll.update(existing, edited);
			output = true;
		} catch (Exception e) {
			output = false;
			log.error("An error has occurred while updating existing user to mongo database", e);
			// TODO: handle exception
		}
		
		return output;
	}
	
	public Boolean deleteUser(String id) {
		boolean output = false;
		log.debug("Deleting existing user; Entered user id is= "+ id);
		try {
			BasicDBObject item = (BasicDBObject) getDBObject(id);
			DBCollection coll = MongoFactory.getCollection(db_name, db_collection);
			
			coll.remove(item);
			output = true;
		} catch (Exception e) {
			log.error("An error occurred while deleting existing user in mongo database",e);
			output = false;
			// TODO: handle exception
		}
		
		return output;
	}
	
	public DBObject getDBObject(String id) {
		DBCollection coll = MongoFactory.getCollection(db_name, db_collection);
		DBObject where_query = new BasicDBObject();
		where_query.put("id",id);
		return coll.findOne(where_query);
	}
	
	public User findUserId(String id) {
		User user = new User();
		DBCollection coll = MongoFactory.getCollection(db_name, db_collection);
		DBObject where_query = new BasicDBObject();
		where_query.put("id", id);
		
		DBObject dbo = coll.findOne(where_query);
		user.setId(dbo.get("id").toString());
		user.setName(dbo.get("name").toString());
		
		return user;
	}
}
