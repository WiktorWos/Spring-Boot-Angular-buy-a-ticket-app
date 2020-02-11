package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TicketController {
    private UserRepository userRepository;

    @Autowired
    public TicketController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/tickets")
    public List<Ticket> findAll(){
        List<User> allUsers = userRepository.findAll();
        List<Ticket> allTickets = getAllTickets(allUsers);;
        return allTickets;
    }

    @GetMapping("/tickets/{userId}")
    public List<Ticket> findUsersTickets(@PathVariable String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Entity not found"));
        return user.getTickets();
    }

    @PutMapping("/tickets/{userId}")
    public Ticket addTicket(@PathVariable String userId, @RequestBody Ticket ticket) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Entity not found"));
        ticket.setTicketValidity(LocalDateTime.now().plusMinutes(50));
        user.getTickets().add(ticket);
        userRepository.save(user);
        return ticket;
    }

    @DeleteMapping("/tickets")
    public List<Ticket> deleteInactiveTickets() {
        List<User> allUsers = userRepository.findAll();
        List<Ticket> allTickets = getAllTickets(allUsers);
        List<Ticket> inactiveTickets = getInactiveTickets(allTickets);

        removeInactiveTickets(allUsers);
        userRepository.saveAll(allUsers);
        return inactiveTickets;
    }

    private List<Ticket> getAllTickets(List<User> allUsers) {
        return allUsers.stream()
                .flatMap(user -> user.getTickets().stream())
                .collect(Collectors.toList());
    }

    private List<Ticket> getInactiveTickets(List<Ticket> allTickets) {
        return allTickets.stream()
                .filter(ticket -> ticket.getTicketValidity().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    private void removeInactiveTickets(List<User> allUsers) {
        allUsers.forEach(user -> user.getTickets()
                .removeIf(this::isTicketInactive));
    }

    private boolean isTicketInactive(Ticket ticket) {
        return ticket.getTicketValidity().isBefore(LocalDateTime.now());
    }
}
