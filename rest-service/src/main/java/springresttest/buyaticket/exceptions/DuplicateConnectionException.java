package springresttest.buyaticket.exceptions;

public class DuplicateConnectionException extends RuntimeException{
    public DuplicateConnectionException() {
        super();
    }

    public DuplicateConnectionException(String message) {
        super(message);
    }

    public DuplicateConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateConnectionException(Throwable cause) {
        super(cause);
    }
}
