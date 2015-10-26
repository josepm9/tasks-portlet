package jpm.mimacom.tasks.portlet.resources.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Response")
@XmlType(propOrder = { "code", "value" })
@XmlAccessorType(XmlAccessType.NONE)
public class Response {
	
	public final static byte[] R_UNKNOWN = "{\"code\":\"P-99999\",\"value\": { \"code\" : null, \"errMsg\" : \"SEVERE SERVER ERROR\" }}".getBytes();

	public static String ERR_GENERIC = "P-99999";
	public static String ERR_TASKSSERVICE = "P-00001";
	public static String ERR_TASKNOTFOUND = "P-00002";

	public Response() {
	}

	public Response(String code, Object value) {
		this.code = code;
		this.value = value;
	}

	@XmlAttribute(name = "code")
	String code;
	@XmlElement(name = "value")
	Object value;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
