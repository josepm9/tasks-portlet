package jpm.mimacom.tasks.bo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="page")
@XmlType(propOrder={"pageNum","pageSize","total","entries"})
@XmlAccessorType(XmlAccessType.NONE)
public class Page<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7656476517645294157L;
	
	@XmlAttribute(name="pageNum")
	long pageNum;
	@XmlAttribute(name="pageSize")
	long pageSize;
	@XmlAttribute(name="total")
	long total;
	@XmlElementWrapper(name="entries")
	@XmlElement(name="entrada")
	List<T> entries;
	@XmlElementWrapper(name="order")
	@XmlElement(name="property")
	List<String> order;
	
	public long getPageNum() {
		return pageNum;
	}
	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}
	public long getPageSize() {
		return pageSize;
	}
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getEntries() {
		return entries;
	}
	public void setEntries(List<T> entries) {
		this.entries = entries;
	}
	public List<String> getOrder() {
		return order;
	}
	public void setOrder(List<String> order) {
		this.order = order;
	}
	
	

}
