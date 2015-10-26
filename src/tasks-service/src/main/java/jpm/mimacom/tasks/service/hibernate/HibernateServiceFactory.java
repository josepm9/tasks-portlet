package jpm.mimacom.tasks.service.hibernate;

import java.io.IOException;

import jpm.mimacom.tasks.service.AbstractServiceFactory;
import jpm.mimacom.tasks.service.TasksService;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateServiceFactory extends AbstractServiceFactory {

	TasksService tasksService;
	ClassPathXmlApplicationContext ctx;

	public HibernateServiceFactory() {
	}

	@Override
	public void close() throws IOException {
		tasksService = null;
		if (ctx != null) {
			try {
				ctx.close();
			} finally {
				ctx = null;
			}
		}
	}

	@Override
	public void initialize() throws Exception {
		final PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setProperties(props);
		ctx = new ClassPathXmlApplicationContext();
		ctx.addBeanFactoryPostProcessor(configurer);
		final String xmlConfig = props.getProperty("spring.config", null);
		if (xmlConfig==null) {
			throw new RuntimeException("Property 'spring.config' is mandatory in hibernate tasks service");
		}
		ctx.setConfigLocation(xmlConfig);
		ctx.refresh();
		tasksService = (TasksService)ctx.getBean("tasksService");
	}

	@Override
	public TasksService getTasksService() {
		return tasksService;
	}

}
