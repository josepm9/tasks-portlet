package jpm.mimacom.tasks;

import java.util.Properties;

import jpm.mimacom.tasks.bo.Page;
import jpm.mimacom.tasks.bo.Task;
import jpm.mimacom.tasks.bo.TaskPk;
import jpm.mimacom.tasks.service.AbstractServiceFactory;
import jpm.mimacom.tasks.service.ServiceException;
import jpm.mimacom.tasks.service.TasksService;

import org.junit.Assert;

public class TestsGenericService {

	final TasksService svc;

	public TestsGenericService(final AbstractServiceFactory fac,
			final String propFile) throws Exception {
		final Properties props = new Properties();
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream(propFile));
		fac.setProps(props);
		fac.initialize();
		this.svc = fac.getTasksService();
	}

	// Tasks create count 50
	final static int TASKS_COUNT = 6;
	final static int USER_TASKS = 3;

	public void run() {
		// Test results holder
		final StringBuilder sbErr = new StringBuilder();

		// Create a user without data!
		new TestWrapper(sbErr, "[testCreateEmptyTask]") {
			public void execute() throws Exception {
				testCreateEmptyTask();
			}
		}.run();
		// Create tasks
		for (int i = 0; i < TASKS_COUNT; ++i) {
			final int insertId = i;
			new TestWrapper(sbErr, "[testCreateTask" + i + "]") {
				public void execute() throws Exception {
					final Task t = generateTask(insertId);
					testCreateTask(t);
				}
			}.run();
		}

		// Search
		for (int i = 0; i < TASKS_COUNT / USER_TASKS; ++i) {
			final int user = i;
			new TestWrapper(sbErr, "[testGetTasks1_user" + i + "]") {
				public void execute() throws Exception {
					testGetTasks(user, 0, 10);
				}
			}.run();
			new TestWrapper(sbErr, "[testGetTasks2_user" + i + "]") {
				public void execute() throws Exception {
					testGetTasks(user, 0, 2);
				}
			}.run();
			new TestWrapper(sbErr, "[testGetTasks3_user" + i + "]") {
				public void execute() throws Exception {
					testGetTasks(user, 1, 2);
				}
			}.run();
			new TestWrapper(sbErr, "[testGetTasks4_user" + i + "]") {
				public void execute() throws Exception {
					testGetTasks(user, 1, 3);
				}
			}.run();
		}
		new TestWrapper(sbErr, "[testGetTasks6_nonexistentuser]") {
			public void execute() throws Exception {
				testGetTasks(TASKS_COUNT / USER_TASKS + 1, 0, 10);
			}
		}.run();

		// TODO: test orders

		// Update the 50 Users created
		for (int i = 0; i < TASKS_COUNT; ++i) {
			final int updateId = i;
			new TestWrapper(sbErr, "[testUpdateTask" + i + "]") {
				public void execute() throws Exception {
					final Task t = generateMutatedTask(updateId);
					testUpdateTask(t);
				}
			}.run();
		}
		// TODO: Test invalid update arguments
		// TODO: Test trying to update unexisting tasks
		// Delete the 50 Users created
		for (int i = 0; i < TASKS_COUNT; ++i) {
			final int deleteId = i;
			new TestWrapper(sbErr, "[testDeleteTask" + i + "]") {
				public void execute() throws Exception {
					testDeleteTask(deleteId);
				}
			}.run();
		}
		// TODO: Test invalid delete arguments
		// TODO: Test trying to delete unexisting tasks
		
		// TODO: Test an user can not operate over tasks he does not own

		// DONE!!
		final String s = sbErr.toString();
		Assert.assertEquals("*** TESTS WITH ERRORS ***" + s + "\n---\n", 0,
				s.length());

	}

	protected void testCreateEmptyTask() throws ServiceException {
		final Task emptyTask = new Task();
		try {
			svc.insertTask(emptyTask);
		} catch (ServiceException e) {
			return; // Expected behaviour. TODO: test error code
		}
		Assert.assertEquals("Empty Task Insertion should have failed", false,
				true);
	}

	protected void testCreateTask(Task t) throws ServiceException {
		Assert.assertEquals(1, svc.insertTask(t));
		// Get and test inserted data
		final Task t2 = svc.getTask(t.getPk());
		Assert.assertEquals(t, t2);
	}

	protected void testGetTasks(int user, int pageIndex, int pageSize)
			throws ServiceException {
		final String userid = "U" + user;
		final Page<Task> page = svc.getTasks(userid, pageIndex, pageSize, null);
		// Get and test inserted data
		final int firstUserTask = user * USER_TASKS;
		final int lastUserTask = (user + 1) * USER_TASKS - 1;
		final int firstTask = pageIndex * pageSize + firstUserTask;
		int lastTask = (pageIndex + 1) * pageSize - 1 + firstUserTask;
		if (lastTask > lastUserTask) {
			lastTask = lastUserTask;
		}
		if (lastTask > TASKS_COUNT - 1) {
			lastTask = TASKS_COUNT - 1;
		}
		int idx = 0;
		Assert.assertEquals("Page index", pageIndex, page.getPageNum());
		Assert.assertEquals("Page size", pageSize, page.getPageSize());
		final int totalExpected = user < (TASKS_COUNT / USER_TASKS) ? USER_TASKS
				: 0;
		Assert.assertEquals("Total calculated", totalExpected, page.getTotal());
		final int presentExpected = (lastTask - firstTask + 1) < 0 ? 0
				: lastTask - firstTask + 1;
		Assert.assertEquals("Total present", presentExpected, page.getEntries()
				.size());
		for (int i = firstTask; i < lastTask; ++i) {
			Assert.assertEquals(generateTask(i), page.getEntries().get(idx++));
		}
	}

	protected void testUpdateTask(Task t) throws ServiceException {
		Assert.assertEquals(1, svc.updateTask(t));
		// Get and test updates data
		final Task t2 = svc.getTask(t.getPk());
		Assert.assertEquals(t, t2);
	}

	protected void testDeleteTask(int i) throws ServiceException {
		Assert.assertEquals(1, svc.delTask(generateTaskPk(i)));
	}

	// Utils

	protected TaskPk generateTaskPk(int i) {
		return new TaskPk("T" + i, "U" + (i / USER_TASKS)) ;
	}

	protected Task generateTask(int i) {
		final Task t = new Task();
		t.setClosed(1000 + i);
		t.setCreated(2000 + i);
		t.setDescription("description" + i);
		t.setPk(generateTaskPk(i));
		t.setLimit(3000 + i);
		t.setState("state" + i);
		t.setSummary("summary" + i);
		return t;
	}

	protected Task generateMutatedTask(int i) {
		final Task t = new Task();
		t.setClosed(10000 + i);
		t.setCreated(20000 + i);
		t.setDescription("description__" + i);
		t.setPk(generateTaskPk(i));
		t.setLimit(30000 + i);
		t.setState("state__" + i);
		t.setSummary("summary__" + i);
		return t;
	}
}
