package jpm.mimacom.tasks.portlet.resources.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="FinishCommand")
@XmlType(propOrder={"taskId"})
@XmlAccessorType(XmlAccessType.NONE)
public class FinishCommand {

	@XmlAttribute(name="taskId")
	String taskId;

	public FinishCommand() {
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
