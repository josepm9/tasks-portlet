package jpm.mimacom.tasks.portlet.resources.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="QueryCommand")
@XmlType(propOrder={"userid","pageNum","pageSize","order"})
@XmlAccessorType(XmlAccessType.NONE)
public class QueryCommand {
	
	@XmlAttribute(name="userid")
	String userid;
	@XmlAttribute(name="pageNum")
	int pageNum;
	@XmlAttribute(name="pageSize")
	int pageSize;
	@XmlElement(name="order")
	List<String> order;

	public QueryCommand() {
	}
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<String> getOrder() {
		return order;
	}

	public void setOrder(List<String> order) {
		this.order = order;
	}

}
