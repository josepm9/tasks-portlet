package jpm.mimacom.tasks.app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TasksAppContextListener 
               implements ServletContextListener{
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (TasksApp.TASKAPP!=null) {
			TasksApp.TASKAPP.stop();
			TasksApp.TASKAPP = null;
		}
	}

        //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		TasksApp.TASKAPP = new TasksApp();
		TasksApp.TASKAPP.start();
	}
}