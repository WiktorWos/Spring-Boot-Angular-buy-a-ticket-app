package springresttest.buyaticket.exceptions;

public class UsedEmailException extends RuntimeException {
    public UsedEmailException() {
        super();
    }

    public UsedEmailException(String message) {
        super(message);
    }

    public UsedEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsedEmailException(Throwable cause) {
        super(cause);
    }
}
