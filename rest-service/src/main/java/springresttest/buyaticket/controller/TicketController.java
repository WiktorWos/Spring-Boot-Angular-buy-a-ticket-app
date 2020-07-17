package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;
import springresttest.buyaticket.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {
    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public List<Ticket> findAll(){
        return ticketService.findAllTickets();
    }

    @GetMapping("/tickets/{userId}")
    public List<Ticket> findUsersTickets(@PathVariable String userId) {
        return ticketService.getUsersTickets(userId);
    }

    @PutMapping("/tickets/{userId}")
    public Ticket addTicket(@PathVariable String userId, @RequestBody Ticket ticket) {
        return ticketService.addTicket(ticket, userId);
    }

    @DeleteMapping("/tickets")
    public List<Ticket> deleteInactiveTickets() {
        List<Ticket> inactiveTickets = ticketService.getInactiveTickets();
        ticketService.deleteInactiveTickets();
        return inactiveTickets;
    }
}
