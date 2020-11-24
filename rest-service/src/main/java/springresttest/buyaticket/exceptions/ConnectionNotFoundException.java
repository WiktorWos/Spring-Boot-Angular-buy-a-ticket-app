package springresttest.buyaticket.exceptions;

public class ConnectionNotFoundException extends RuntimeException {
    public ConnectionNotFoundException() {
        super();
    }

    public ConnectionNotFoundException(String message) {
        super(message);
    }

    public ConnectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionNotFoundException(Throwable cause) {
        super(cause);
    }
}
