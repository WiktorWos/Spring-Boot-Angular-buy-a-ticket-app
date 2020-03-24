package springresttest.buyaticket.controller;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.pdf.PdfGenerator;
import springresttest.buyaticket.repository.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TicketController {
    private UserRepository userRepository;
    private PdfGenerator pdfGenerator;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Autowired
    public TicketController(UserRepository userRepository, PdfGenerator pdfGenerator) {
        this.userRepository = userRepository;
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping("/tickets")
    public List<Ticket> findAll(){
        List<User> allUsers = userRepository.findAll();
        List<Ticket> allTickets = getAllTickets(allUsers);
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
        int ticketDuration = ticket.getType().getDuration();
        ticket.setTicketValidity(LocalDateTime.now().plusMinutes(ticketDuration));
        user.getTickets().add(ticket);
        generatePdf(user, ticket);
        userRepository.save(user);
        return ticket;
    }



    private void generatePdf(User user, Ticket ticket){
        try {
            pdfGenerator.generatePfd(user, ticket);
        } catch (IOException|DocumentException|URISyntaxException e) {
            e.printStackTrace();
        }
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
