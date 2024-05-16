package de.lukegoll.personalverzeichnis.domain.exceptions;

public class TimesheetServiceException extends RuntimeException{


	private static final long serialVersionUID = -605990179454074384L;


	public TimesheetServiceException() {
        super();
    }


    public TimesheetServiceException(String message) {
        super(message);
    }


    public TimesheetServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimesheetServiceException(Throwable cause) {
        super(cause);
    }


    protected TimesheetServiceException(String message, Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
