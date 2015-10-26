package jpm.mimacom.tasks.service.mongo;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;

import jpm.mimacom.tasks.service.AbstractServiceFactory;
import jpm.mimacom.tasks.service.TasksService;
import jpm.mimacom.tasks.util.TrustSocketFactory;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoServiceFactory extends AbstractServiceFactory {

	MongoClient mongoClient = null;
	MongoDatabase mongoDB = null;
	MongoCollection<Document> tasksColl;

	String db = "tasks";
	String col = "tasks";
	String user;
	String password;
	String host;
	int port = 27017;

	boolean ssl = true;
	boolean invalidHostNameAllowed = false;
	boolean trustAnyCert = false;

	final TasksService tasksService;

	public MongoServiceFactory() {
		tasksService = new TasksMongoService(this);
	}

	protected void configure(Properties p) {
		String s = p.getProperty("mongo.db", null);
		if (s != null) {
			db = s;
		}
		s = p.getProperty("mongo.col", null);
		if (s != null) {
			col = s;
		}
		s = p.getProperty("mongo.user", null);
		if (s != null) {
			user = s;
		}
		s = p.getProperty("mongo.password", null);
		if (s != null) {
			password = s;
		}
		s = p.getProperty("mongo.host", null);
		if (s != null) {
			host = s;
		}
		s = p.getProperty("mongo.port", null);
		if (s != null) {
			port = Integer.parseInt(s, 10);
		}
		s = p.getProperty("mongo.invalidHostNameAllowed", null);
		if (s != null) {
			invalidHostNameAllowed = "true".equals(s);
		}
		s = p.getProperty("mongo.trustAnyCert", null);
		if (s != null) {
			trustAnyCert = "true".equals(s);
		}
	}

	// Closeable
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	@Override
	public void initialize() throws KeyManagementException,
			NoSuchAlgorithmException {
		if (mongoClient != null) {
			close();
		}
		if (props != null) {
			configure(props);
		}
		final ArrayList<MongoCredential> credentialList = new ArrayList<MongoCredential>();
		credentialList.add(MongoCredential.createCredential(user, db,
				password.toCharArray()));
		Builder opsBuilder = MongoClientOptions.builder().sslEnabled(ssl)
				.sslInvalidHostNameAllowed(invalidHostNameAllowed);
		if (trustAnyCert) {
			opsBuilder = opsBuilder.socketFactory(TrustSocketFactory
					.createSocketFactory());
		}
		mongoClient = new MongoClient(new ServerAddress(host, port),
				credentialList, opsBuilder.build());
		mongoDB = mongoClient.getDatabase(db);
		tasksColl = mongoDB.getCollection(col);
	}

	@Override
	public TasksService getTasksService() {
		return tasksService;
	}

}
