package springresttest.buyaticket.model;


import java.time.LocalDateTime;


public class Ticket {
    private String ticketId;
    private String type;
    private LocalDateTime ticketValidity;

    public Ticket() {
    }

    public Ticket(String type, LocalDateTime ticketValidity) {
        this.type = type;
        this.ticketValidity = ticketValidity;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTicketValidity() {
        return ticketValidity;
    }

    public void setTicketValidity(LocalDateTime ticketValidity) {
        this.ticketValidity = ticketValidity;
    }
}
