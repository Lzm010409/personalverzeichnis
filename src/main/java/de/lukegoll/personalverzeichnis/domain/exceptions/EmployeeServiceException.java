package de.lukegoll.personalverzeichnis.domain.exceptions;

public class EmployeeServiceException extends RuntimeException{

	
	private static final long serialVersionUID = -605990179454074384L;


	public EmployeeServiceException() {
        super();
    }

   
    public EmployeeServiceException(String message) {
        super(message);
    }

   
    public EmployeeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeServiceException(Throwable cause) {
        super(cause);
    }

    
    protected EmployeeServiceException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
