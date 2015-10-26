package jpm.mimacom.tasks.portlet.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import jpm.mimacom.tasks.bo.Page;
import jpm.mimacom.tasks.bo.Task;
import jpm.mimacom.tasks.bo.TaskPk;
import jpm.mimacom.tasks.portlet.resources.api.DeleteCommand;
import jpm.mimacom.tasks.portlet.resources.api.FinishCommand;
import jpm.mimacom.tasks.portlet.resources.api.GetCommand;
import jpm.mimacom.tasks.portlet.resources.api.InsertCommand;
import jpm.mimacom.tasks.portlet.resources.api.QueryCommand;
import jpm.mimacom.tasks.portlet.resources.api.Response;
import jpm.mimacom.tasks.portlet.resources.api.UpdateCommand;
import jpm.mimacom.tasks.service.ServiceException;
import jpm.mimacom.tasks.service.TasksService;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class TasksResources {

	final ObjectMapper mapper;
	TasksService tasksService;

	public TasksResources() {
		mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.getDeserializationConfig().withAnnotationIntrospector(
				introspector);
		// make serializer use JAXB annotations (only)
		mapper.getSerializationConfig()
				.withAnnotationIntrospector(introspector);
	}

	public TasksService getTasksService() {
		return tasksService;
	}

	public void setTasksService(TasksService tasksService) {
		this.tasksService = tasksService;
	}

	public void dispatch(String userLoginId, String cmd, String entity,
			OutputStream out) throws IOException {
		dispatch(userLoginId, cmd, entity == null ? null
				: new ByteArrayInputStream(entity.getBytes()), out);
	}

	public void dispatch(String userLoginId, String cmd, InputStream entity,
			OutputStream out) throws IOException {
		try {
			if ("Delete".equals(cmd)) {
				mapper.writeValue(
						out,
						dispatch(userLoginId,
								mapper.readValue(entity, DeleteCommand.class)));
			} else if ("Finish".equals(cmd)) {
				mapper.writeValue(
						out,
						dispatch(userLoginId,
								mapper.readValue(entity, FinishCommand.class)));
			} else if ("Get".equals(cmd)) {
				mapper.writeValue(
						out,
						dispatch(userLoginId,
								mapper.readValue(entity, GetCommand.class)));
			} else if ("Insert".equals(cmd)) {
				mapper.writeValue(
						out,
						dispatch(userLoginId,
								mapper.readValue(entity, InsertCommand.class)));
			} else if ("Query".equals(cmd)) {
				mapper.writeValue(
						out,
						dispatch(userLoginId,
								mapper.readValue(entity, QueryCommand.class)));
			} else if ("Update".equals(cmd)) {
				mapper.writeValue(
						out,
						dispatch(userLoginId,
								mapper.readValue(entity, UpdateCommand.class)));
			} else {

			}
		} catch (ServiceException s) {
			try {
				mapper.writeValue(out, new Response(Response.ERR_TASKSSERVICE,
						s.getServiceError()));
			} catch (Throwable e) {
				out.write(Response.R_UNKNOWN);
			}

		} catch (Throwable t) {
			try {
				mapper.writeValue(out, new Response(Response.ERR_GENERIC,
						new ServiceException(t).getServiceError()));
			} catch (Throwable e) {
				out.write(Response.R_UNKNOWN);
			}
		}
	}

	public Object dispatch(String userLoginId, DeleteCommand cmd)
			throws ServiceException {
		final int i = tasksService.delTask(new TaskPk(cmd.getTaskId(), userLoginId) );
		if (i == 1) {
			return new Response("0", null);
		} else {
			return new Response(Response.ERR_TASKNOTFOUND, "Task "
					+ cmd.getTaskId() + " not found");
		}
	}
	
	public Object dispatch(String userLoginId, FinishCommand cmd)
			throws ServiceException {
		final int i = tasksService.finishTask(new TaskPk(cmd.getTaskId(), userLoginId));
		if (i == 1) {
			return new Response("0", null);
		} else {
			return new Response(Response.ERR_TASKNOTFOUND, "Task "
					+ cmd.getTaskId() + " not found");
		}
	}

	public Object dispatch(String userLoginId, GetCommand cmd)
			throws ServiceException {
		final Task t = tasksService.getTask(new TaskPk(cmd.getTaskId(), userLoginId));
		if (t != null) {
			return new Response("0", t);
		} else {
			return new Response(Response.ERR_TASKNOTFOUND, "Task "
					+ cmd.getTaskId() + " not found");
		}
	}

	public Object dispatch(String userLoginId, InsertCommand cmd)
			throws ServiceException {
		cmd.getTask().setCreated(new Date().getTime());
		cmd.getTask().getPk().setUserid(userLoginId);
		final int i = tasksService.insertTask(cmd.getTask());
		if (i==1) {
			return new Response("0", i);
		} else {
			return new Response(Response.ERR_GENERIC, "Task not inserted");
		}
	}

	public Object dispatch(String userLoginId, QueryCommand cmd)
			throws ServiceException {
		final Page<Task> page = tasksService.getTasks(userLoginId,
				cmd.getPageNum(), cmd.getPageSize(), cmd.getOrder());
		if (page != null) {
			return new Response("0", page);
		} else {
			return new Response(Response.ERR_GENERIC, "Results not retrieved");
		}
	}

	public Object dispatch(String userLoginId, UpdateCommand cmd)
			throws ServiceException {
		cmd.getTask().getPk().setUserid(userLoginId);
		final int i = tasksService.updateTask(cmd.getTask());
		if (i == 1) {
			return new Response("0", null);
		} else {
			return new Response(Response.ERR_TASKNOTFOUND, "Task "
					+ cmd.getTask().getPk() + " not found");
		}
	}
}
