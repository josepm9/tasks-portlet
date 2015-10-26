package jpm.mimacom.tasks.service;


public class ServiceException extends Exception {

	public static String ERROR_GENERIC = "G-9900";
	public static String ERROR_GENERIC_INSERT = "G-9901";
	public static String ERROR_GENERIC_UPDATE = "G-9902";
	public static String ERROR_GENERIC_DELETE = "G-9903";
	public static String ERROR_GENERIC_QUERY = "G-9904";
	public static String ERROR_GENERIC_GET = "G-9905";

	public static String ERROR_ENTITY_NOT_FOUND = "G-0001";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1446712344261187855L;

	protected SerializableServiceError serviceError = new SerializableServiceError();

	public ServiceException() {
		serviceError.code = ERROR_GENERIC;
	}

	public ServiceException(String message) {
		super(message);
		serviceError.code = ERROR_GENERIC;
		serviceError.errMsg = message;
	}

	public ServiceException(Throwable cause) {
		super(cause);
		serviceError.code = ERROR_GENERIC;
		serviceError.innerError = SerializableServiceError.fromThrowable(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		serviceError.code = ERROR_GENERIC;
		serviceError.innerError = SerializableServiceError.fromThrowable(cause);
		serviceError.errMsg = message;
	}

	public ServiceException(String code, String message, Throwable cause) {
		super(message, cause);
		serviceError.code = code;
		serviceError.innerError = SerializableServiceError.fromThrowable(cause);
		serviceError.errMsg = message;
	}

	public SerializableServiceError getServiceError() {
		return serviceError;
	}

}
