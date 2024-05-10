package de.lukegoll.personalverzeichnis.domain.exceptions;

public class CapabilityServiceException extends RuntimeException{

	
	private static final long serialVersionUID = -605990179454074384L;


	public CapabilityServiceException() {
        super();
    }

   
    public CapabilityServiceException(String message) {
        super(message);
    }

   
    public CapabilityServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CapabilityServiceException(Throwable cause) {
        super(cause);
    }

    
    protected CapabilityServiceException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
