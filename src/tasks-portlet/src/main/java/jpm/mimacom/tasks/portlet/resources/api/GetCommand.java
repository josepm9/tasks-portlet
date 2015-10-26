package jpm.mimacom.tasks.portlet.resources.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="GetCommand")
@XmlType(propOrder={"taskId"})
@XmlAccessorType(XmlAccessType.NONE)
public class GetCommand {

	@XmlAttribute(name="taskId")
	String taskId;

	public GetCommand() {
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
