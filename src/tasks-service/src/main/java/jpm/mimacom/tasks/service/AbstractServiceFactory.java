package jpm.mimacom.tasks.service;

import java.io.Closeable;
import java.util.Properties;

public abstract class AbstractServiceFactory implements Closeable {

	protected Properties props;

	public Properties getProps() {
		return props;
	}

	public void setProps(final Properties props) {
		this.props = props;
	}

	public abstract void initialize() throws Exception;

	public abstract TasksService getTasksService();

}
