package jpm.mimacom.tasks.service;

import java.util.List;

import jpm.mimacom.tasks.bo.Page;
import jpm.mimacom.tasks.bo.Task;
import jpm.mimacom.tasks.bo.TaskPk;

public interface TasksService {

	public Page<Task> getTasks(String userid, int pageNum, int pageSize, List<String> order) throws ServiceException;
	public Task getTask(TaskPk pk) throws ServiceException;
	public int delTask(TaskPk pk) throws ServiceException;
	public int updateTask(Task t) throws ServiceException;
	public int finishTask(TaskPk pk) throws ServiceException;
	public int insertTask(Task t) throws ServiceException;
}
