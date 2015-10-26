package jpm.mimacom.tasks.service.hibernate;

import java.util.Date;
import java.util.List;

import jpm.mimacom.tasks.bo.Page;
import jpm.mimacom.tasks.bo.Task;
import jpm.mimacom.tasks.bo.TaskPk;
import jpm.mimacom.tasks.service.ServiceException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class HibernateTasksDAO {

	final Session session;

	public HibernateTasksDAO(final Session session) {
		this.session = session;
	}

	public Page<Task> getTasks(String userid, int pageNum, int pageSize,
			List<String> order) throws ServiceException {
		final Criteria criteria = session.createCriteria(Task.class);
		criteria.add(Restrictions.eq("pk.userid", userid));
		criteria.setFirstResult(pageNum * pageSize);
		criteria.setMaxResults(pageSize);
		if (order != null && order.size() > 0) {
			addOrder(order, criteria);
		}
		criteria.setReadOnly(true);
		//
		@SuppressWarnings("unchecked")
		final List<Task> entries = (List<Task>) criteria.list();
		final Page<Task> p = new Page<Task>();
		p.setTotal((Long) session.createCriteria(Task.class)
				.add(Restrictions.eq("pk.userid", userid))
				.setProjection(Projections.rowCount()).uniqueResult());
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setEntries(entries);
		p.setOrder(order);
		return p;
	}

	protected Criteria addOrder(final List<String> orden,
			final Criteria criteria) {
		for (String s : orden) {
			int asc = 1;
			final int idx = s.indexOf(" ");
			if (idx > 0) {
				asc = " asc".equals(s.substring(idx)) ? 1 : -1;
				s = s.substring(0, idx);
			}
			criteria.addOrder(asc == 1 ? Order.asc(s) : Order.desc(s));
		}
		return criteria;

	}

	public Task getTask(TaskPk pk) throws ServiceException {
		final Criteria criteria = session.createCriteria(Task.class)
				.add(Restrictions.eq("pk", pk));
		return (Task) criteria.uniqueResult();
	}

	public int delTask(TaskPk pk) throws ServiceException {
		final Task t = new Task();
		t.setPk(pk);
		try {
			session.delete(t);
			return 1;
		} catch (HibernateException e) {
			throw new ServiceException(e);
		}
	}

	public int finishTask(TaskPk pk) throws ServiceException {
		final Task t = getTask(pk);
		if (t == null) {
			throw new ServiceException(ServiceException.ERROR_ENTITY_NOT_FOUND,
					"Task '" + pk + "' not found", null);
		}
		try {
			t.setClosed(new Date().getTime());
			t.setState("Finished");
			session.update(t);
			return 1;
		} catch (HibernateException e) {
			throw new ServiceException(e);
		}
	}

	public int updateTask(Task t) throws ServiceException {
		try {
			session.update(t);
			return 1;
		} catch (HibernateException e) {
			throw new ServiceException(e);
		}
	}

	public int insertTask(Task t) throws ServiceException {
		try {
			session.save(t);
			return 1;
		} catch (HibernateException e) {
			throw new ServiceException(e);
		}
	}

}
