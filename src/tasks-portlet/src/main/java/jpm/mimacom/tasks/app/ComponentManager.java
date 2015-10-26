package jpm.mimacom.tasks.app;

import jpm.mimacom.tasks.portlet.resources.TasksResources;
import jpm.mimacom.tasks.service.TasksService;

public class ComponentManager {
	
	static ComponentManager INSTANCE;
	
	TasksService tasksService = null;
	TasksResources tasksResources= null;

	ComponentManager() {
		INSTANCE = this;
	}
	
	public static ComponentManager getInstance() {
		return INSTANCE;
	}
	
	public TasksService getTasksService() {
		return tasksService;
	}
	
	public TasksResources getTasksResources() {
		return tasksResources;
	}

}
