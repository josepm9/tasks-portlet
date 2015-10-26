package jpm.mimacom.tasks.bo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="Task")
@XmlType(propOrder={"pk","created","limit","closed","state","summary","description"})
@XmlAccessorType(XmlAccessType.NONE)
public class Task implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -555886050677082380L;
	
	@XmlElement(name="pk")
	protected TaskPk pk;
	@XmlAttribute(name="created")
	protected Long created;
	@XmlAttribute(name="limit")
	protected Long limit;
	@XmlAttribute(name="closed")
	protected Long closed;
	@XmlAttribute(name="state")
	protected String state;
	@XmlAttribute(name="summary")
	protected String summary;
	@XmlAttribute(name="description")
	protected String description;
	public TaskPk getPk() {
		return pk;
	}
	public void setPk(TaskPk pk) {
		this.pk = pk;
	}
	public Long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public Long getLimit() {
		return limit;
	}
	public void setLimit(long limit) {
		this.limit = limit;
	}
	public Long getClosed() {
		return closed;
	}
	public void setClosed(long closed) {
		this.closed = closed;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closed == null) ? 0 : closed.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
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
		Task other = (Task) obj;
		if (closed == null) {
			if (other.closed != null)
				return false;
		} else if (!closed.equals(other.closed))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (limit == null) {
			if (other.limit != null)
				return false;
		} else if (!limit.equals(other.limit))
			return false;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		return true;
	}
	
	

}
