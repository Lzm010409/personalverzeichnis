package de.lukegoll.personalverzeichnis.domain.exceptions;

public class EmploymentServiceException extends RuntimeException{

	
	private static final long serialVersionUID = -605990179454074384L;


	public EmploymentServiceException() {
        super();
    }

   
    public EmploymentServiceException(String message) {
        super(message);
    }

   
    public EmploymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmploymentServiceException(Throwable cause) {
        super(cause);
    }

    
    protected EmploymentServiceException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
