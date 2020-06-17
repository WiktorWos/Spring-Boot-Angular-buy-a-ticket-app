package springresttest.buyaticket.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ticket {
    private TicketType type;
    private LocalDateTime ticketValidity;

    public Ticket() {
    }

    public Ticket(TicketType type) {
        this.type = type;
    }

    public Ticket(TicketType type, LocalDateTime ticketValidity) {
        this.type = type;
        this.ticketValidity = ticketValidity;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    public LocalDateTime getTicketValidity() {
        return ticketValidity;
    }

    public void setTicketValidity(LocalDateTime ticketValidity) {
        this.ticketValidity = ticketValidity;
    }
}
