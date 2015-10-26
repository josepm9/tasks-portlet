package jpm.mimacom.tasks;

import jpm.mimacom.tasks.service.mongo.MongoServiceFactory;

import org.junit.Test;

public class TestsMongoService {

	@Test
	public void testsMongo() throws Exception {
		// Initialize
		final MongoServiceFactory f = new MongoServiceFactory();
		try {
			new TestsGenericService(f, "tasks-svc-mongo.properties").run();
		} finally {
			f.close();
		}
	}

}
