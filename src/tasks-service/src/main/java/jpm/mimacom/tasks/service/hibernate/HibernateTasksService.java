package jpm.mimacom.tasks.service.hibernate;

import java.util.List;

import jpm.mimacom.tasks.bo.Page;
import jpm.mimacom.tasks.bo.Task;
import jpm.mimacom.tasks.bo.TaskPk;
import jpm.mimacom.tasks.service.ServiceException;
import jpm.mimacom.tasks.service.TasksService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateTasksService implements TasksService {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public HibernateTasksService() {
	}

	@Override
	public Page<Task> getTasks(String userid, int pageNum, int pageSize,
			List<String> order) throws ServiceException {
		try {
			final Session session = sessionFactory.openSession();
			try {
				return new HibernateTasksDAO(session).getTasks(userid, pageNum,
						pageSize, order);

			} finally {
				session.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_QUERY,
					t.getMessage(), t);
		}
	}

	@Override
	public Task getTask(TaskPk pk) throws ServiceException {
		try {
			final Session session = sessionFactory.openSession();
			try {
				return new HibernateTasksDAO(session).getTask(pk);
			} finally {
				session.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_GET,
					t.getMessage(), t);
		}
	}

	@Override
	public int delTask(TaskPk pk) throws ServiceException {
		try {
			final Session session = sessionFactory.openSession();
			try {
				final Transaction tx = session.beginTransaction();
				final int i = new HibernateTasksDAO(session).delTask(pk);
				tx.commit();
				return i;
			} finally {
				session.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_DELETE,
					t.getMessage(), t);
		}
	}
	
	@Override
	public int finishTask(TaskPk pk) throws ServiceException {
		try {
			final Session session = sessionFactory.openSession();
			try {
				final Transaction tx = session.beginTransaction();
				final int i = new HibernateTasksDAO(session).finishTask(pk);
				tx.commit();
				return i;
			} finally {
				session.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_DELETE,
					t.getMessage(), t);
		}
	}

	@Override
	public int updateTask(Task task) throws ServiceException {
		try {
			final Session session = sessionFactory.openSession();
			try {
				final Transaction tx = session.beginTransaction();
				final int i = new HibernateTasksDAO(session).updateTask(task);
				tx.commit();
				return i;
			} finally {
				session.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_UPDATE,
					t.getMessage(), t);
		}
	}

	@Override
	public int insertTask(Task task) throws ServiceException {
		try {
			final Session session = sessionFactory.openSession();
			try {
				final Transaction tx = session.beginTransaction();
				final int i = new HibernateTasksDAO(session)
						.insertTask(task);
				tx.commit();
				return i;
			} finally {
				session.close();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServiceException(ServiceException.ERROR_GENERIC_INSERT,
					t.getMessage(), t);
		}
	}

}
