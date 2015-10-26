package jpm.mimacom.tasks;

import jpm.mimacom.tasks.service.hibernate.HibernateServiceFactory;

import org.junit.Test;

public class TestsHibernateService {

	@Test
	public void testsHibernate() throws Exception {
		// Initialize
		final HibernateServiceFactory f = new HibernateServiceFactory();
		try {
			new TestsGenericService(f, "tasks-svc-h2.properties").run();
		} finally {
			f.close();
		}
	}

}
