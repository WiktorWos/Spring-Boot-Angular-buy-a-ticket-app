package springresttest.buyaticket.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ticket {
    private String type;

    private LocalDateTime ticketValidity;

    public Ticket() {
    }

    public Ticket(String type) {
        this.type = type;
    }

    public Ticket(String type, LocalDateTime ticketValidity) {
        this.type = type;
        this.ticketValidity = ticketValidity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
