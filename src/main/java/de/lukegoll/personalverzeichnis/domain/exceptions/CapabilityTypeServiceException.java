package de.lukegoll.personalverzeichnis.domain.exceptions;

public class CapabilityTypeServiceException extends RuntimeException{

	
	private static final long serialVersionUID = -605990179454074384L;


	public CapabilityTypeServiceException() {
        super();
    }

   
    public CapabilityTypeServiceException(String message) {
        super(message);
    }

   
    public CapabilityTypeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CapabilityTypeServiceException(Throwable cause) {
        super(cause);
    }

    
    protected CapabilityTypeServiceException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
