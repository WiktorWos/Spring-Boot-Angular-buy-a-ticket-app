package springresttest.buyaticket.exceptions;

import java.util.List;

public class ErrorResponse {
    private int status;
    private List<String> messages;
    private long timestamp;

    public ErrorResponse(int status, List<String> messages, long timestamp) {
        this.status = status;
        this.messages = messages;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getMessage() {
        return messages;
    }

    public void setMessage(List<String> messages) {
        this.messages = messages;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
