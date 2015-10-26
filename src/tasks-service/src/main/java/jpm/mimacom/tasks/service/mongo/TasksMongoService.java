package jpm.mimacom.tasks.service.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jpm.mimacom.tasks.bo.Page;
import jpm.mimacom.tasks.bo.Task;
import jpm.mimacom.tasks.bo.TaskPk;
import jpm.mimacom.tasks.service.ServiceException;
import jpm.mimacom.tasks.service.TasksService;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

public class TasksMongoService implements TasksService {

	final MongoServiceFactory f;

	public TasksMongoService(final MongoServiceFactory f) {
		this.f = f;
	}

	private Document getPk(TaskPk pk) {
		return new Document("_id", new Document("id", pk.getId()).append(
				"userid", pk.getUserid()));
	}

	private Document getUserFilter(String userid) {
		return new Document("_id.userid", userid);
	}

	@Override
	public Page<Task> getTasks(String userid, int pageNum, int pageSize,
			List<String> order) throws ServiceException {
		try {
			final Document filter = getUserFilter(userid);
			FindIterable<Document> fi = f.tasksColl.find(filter);
			if (order != null && order.size() > 0) {
				fi = addOrder(order, fi);
			}
			final MongoCursor<Document> cur = fi.skip(pageNum * pageSize)
					.limit(pageSize).iterator();
			final Page<Task> p = new Page<Task>();
			p.setTotal(f.tasksColl.count(filter));
			p.setPageNum(pageNum);
			p.setPageSize(pageSize);
			p.setEntries(toTasks(cur));
			p.setOrder(order);
			return p;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_QUERY,
					t.getMessage(), t);
		}
	}

	@Override
	public Task getTask(TaskPk pk) throws ServiceException {
		try {
			final Document d = f.tasksColl.find(getPk(pk)).first();
			return d == null ? null : toTask(d);
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_GET,
					t.getMessage(), t);
		}
	}

	@Override
	public int delTask(TaskPk pk) throws ServiceException {
		try {
			return (int) f.tasksColl.deleteOne(getPk(pk)).getDeletedCount();
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_DELETE,
					t.getMessage(), t);
		}
	}

	@Override
	public int updateTask(Task task) throws ServiceException {
		try {
			return (int) f.tasksColl.updateOne(getPk(task.getPk()),
					new Document("$set", toDocument(task))).getModifiedCount();
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_UPDATE,
					t.getMessage(), t);
		}

	}

	@Override
	public int finishTask(TaskPk pk) throws ServiceException {
		try {
			final Document updated = new Document("closed",
					new Date().getTime()).append("state", "Finished");
			return (int) f.tasksColl.updateOne(getPk(pk),
					new Document("$set", updated)).getModifiedCount();
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_UPDATE,
					t.getMessage(), t);
		}

	}

	@Override
	public int insertTask(Task task) throws ServiceException {
		try {
			// ID MUST BE SET!
			if (task.getPk() == null || task.getPk().getId()==null || task.getPk().getUserid()==null) {
				throw new ServiceException("Task ID is mandatory");
			}
			final Document d = toDocument(task);
			f.tasksColl.insertOne(d);
			return 1;
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_INSERT,
					t.getMessage(), t);
		}

	}

	// Utils

	protected Document toDocument(Task t) {
		final Document d = getPk(t.getPk());
		d.append("closed", t.getClosed());
		d.append("created", t.getCreated());
		d.append("description", t.getDescription());
		d.append("state", t.getState());
		d.append("limit", t.getLimit());
		d.append("summary", t.getSummary());
		return d;
	}

	protected Task toTask(final Document d) {
		final Task t = new Task();
		t.setClosed(d.getLong("closed"));
		t.setCreated(d.getLong("created"));
		t.setDescription(d.getString("description"));
		t.setState(d.getString("state"));
		t.setPk(toTaskPk(d.get("_id", Document.class)));
		t.setLimit(d.getLong("limit"));
		t.setSummary(d.getString("summary"));
		return t;
	}
	
	protected TaskPk toTaskPk(final Document d) {
		return new TaskPk(d.getString("id"), d.getString("userid"));
		
	}

	protected List<Task> toTasks(final MongoCursor<Document> cur) {
		final ArrayList<Task> tasks = new ArrayList<Task>();
		while (cur.hasNext()) {
			tasks.add(toTask(cur.next()));
		}
		return tasks;
	}

	protected FindIterable<Document> addOrder(final List<String> orden,
			final FindIterable<Document> fi) {
		final BasicDBObject orderObj = new BasicDBObject();
		for (String s : orden) {
			int asc = 1;
			final int idx = s.indexOf(" ");
			if (idx > 0) {
				asc = " asc".equals(s.substring(idx)) ? 1 : -1;
				s = s.substring(0, idx);
			}
			orderObj.append(s, asc);
		}
		return fi.sort(orderObj);

	}
}
