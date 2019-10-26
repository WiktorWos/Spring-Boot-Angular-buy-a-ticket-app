package springresttest.buyaticket.ticket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Ticket {
    @Id
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
