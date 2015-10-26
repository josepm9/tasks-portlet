package jpm.mimacom.tasks.app;

import java.io.InputStream;
import java.util.Properties;

import jpm.mimacom.tasks.portlet.resources.TasksResources;
import jpm.mimacom.tasks.service.AbstractServiceFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class TasksApp {

	final static Logger LOGGER = LogManager.getLogger(TasksApp.class);
	static TasksApp TASKAPP;

	Properties appProperties;
	AbstractServiceFactory tasksSvcFac;

	TasksApp() {
	}

	private void loadFromClasspath(String file) {
		try {
			final InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream(file);
			if (is == null) {
				LOGGER.warn("Could not load configuration from '" + file
						+ "', not found");
				return;
			}
			try {
				appProperties.load(is);
			} finally {
				try {
					is.close();
				} catch (Throwable t) {
				}
			}
		} catch (Throwable t) {
			LOGGER.error("Error while loading configuration '" + file + "': "
					+ t.getMessage(), t);
		}
	}

	// initialize && finalize

	public void start() {
		try {
			// Load properties
			appProperties = new Properties();
			loadFromClasspath("tasks-default.properties");
			loadFromClasspath("tasks-ex.properties");
			final String additional = appProperties.getProperty(
					"tasks.properties.aditional", null);
			if (additional != null && additional.length() > 0) {
				loadFromClasspath(additional);
			}
			// Initialize
			final ComponentManager man = new ComponentManager();
			// Configure TasksService
			final String impl = appProperties.getProperty("tasks.service.implementation", null);
			if (impl==null) {
				throw new RuntimeException("Property 'tasks.service.implementation' is mandatory!");
			}
			tasksSvcFac = (AbstractServiceFactory)Class.forName(impl).newInstance();
			tasksSvcFac.setProps(appProperties);
			tasksSvcFac.initialize();
			man.tasksService = tasksSvcFac.getTasksService();
			man.tasksResources = new TasksResources();
			man.tasksResources.setTasksService(man.tasksService);
		} catch (Throwable t) {
			LOGGER.error("Exception in initializer: " + t.getMessage(), t);
			stop();
		}
	}

	public void stop() {
		appProperties = null;
		ComponentManager.INSTANCE = null;
		if (tasksSvcFac != null) {
			try {
				tasksSvcFac.close();
			} catch (Throwable t) {
				LOGGER.error("Exception in finalizer: " + t.getMessage(), t);
			}
			tasksSvcFac = null;
		}
	}

	protected void finalize() {
		stop();
	}

}
