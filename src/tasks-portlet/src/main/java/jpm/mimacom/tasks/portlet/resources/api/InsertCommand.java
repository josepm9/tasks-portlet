package jpm.mimacom.tasks.portlet.resources.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jpm.mimacom.tasks.bo.Task;

@XmlRootElement(name="InsertCommand")
@XmlType(propOrder={"task"})
@XmlAccessorType(XmlAccessType.NONE)
public class InsertCommand {
	
	@XmlAttribute(name="task")
	Task task;

	public InsertCommand() {
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
