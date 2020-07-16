package springresttest.buyaticket.exceptions;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super();
    }

    public WrongCredentialsException(String message) {
        super(message);
    }

    public WrongCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongCredentialsException(Throwable cause) {
        super(cause);
    }
}
