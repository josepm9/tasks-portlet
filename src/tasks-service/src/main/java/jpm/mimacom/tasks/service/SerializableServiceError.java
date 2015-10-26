package jpm.mimacom.tasks.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SerializableServiceError")
@XmlType(propOrder = { "code", "errMsg", "innerError" })
@XmlAccessorType(XmlAccessType.NONE)
public class SerializableServiceError {
	@XmlAttribute(name = "code")
	String code;
	@XmlAttribute(name = "errMsg")
	String errMsg;
	@XmlElement(name = "innerError")
	SerializableServiceError innerError;

	public static SerializableServiceError fromThrowable(Throwable t) {
		final SerializableServiceError e = new SerializableServiceError();
		e.errMsg = t.getClass().getName() + ": " + t.getMessage();
		e.innerError = t.getCause() == null ? null : fromThrowable(t
				.getCause());
		return e;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public SerializableServiceError getInnerError() {
		return innerError;
	}

	public void setInnerError(SerializableServiceError innerError) {
		this.innerError = innerError;
	}
	
}