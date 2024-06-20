package de.lukegoll.personalverzeichnis.domain.exceptions;

public class DocumentServiceException extends RuntimeException{


	private static final long serialVersionUID = -605990179454074384L;


	public DocumentServiceException() {
        super();
    }


    public DocumentServiceException(String message) {
        super(message);
    }


    public DocumentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentServiceException(Throwable cause) {
        super(cause);
    }


    protected DocumentServiceException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
