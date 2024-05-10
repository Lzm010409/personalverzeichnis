package de.lukegoll.personalverzeichnis.domain.exceptions;

public class DepartmentServiceException extends RuntimeException{

	
	private static final long serialVersionUID = -605990179454074384L;


	public DepartmentServiceException() {
        super();
    }

   
    public DepartmentServiceException(String message) {
        super(message);
    }

   
    public DepartmentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartmentServiceException(Throwable cause) {
        super(cause);
    }

    
    protected DepartmentServiceException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
