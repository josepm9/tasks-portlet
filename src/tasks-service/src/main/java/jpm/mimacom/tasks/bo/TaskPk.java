package jpm.mimacom.tasks.bo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TaskPk")
@XmlType(propOrder = { "id", "userid" })
@XmlAccessorType(XmlAccessType.NONE)
public class TaskPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3732145797252713656L;

	public TaskPk() {
	}

	public TaskPk(final String id, final String userid) {
		this.id = id;
		this.userid = userid;

	}

	@XmlAttribute(name = "id")
	protected String id;
	@XmlAttribute(name = "userid")
	protected String userid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskPk other = (TaskPk) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userid == null) {
			if (other.userid != null)
				return false;
		} else if (!userid.equals(other.userid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "@TaskPk{id:" + id + ", userid:" + userid + "}";
	}

}
