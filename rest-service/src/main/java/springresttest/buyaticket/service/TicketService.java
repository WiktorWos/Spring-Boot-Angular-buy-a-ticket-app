package springresttest.buyaticket.service;

import org.springframework.stereotype.Service;
import springresttest.buyaticket.email.EmailSender;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.TicketType;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private UserRepository userRepository;
    private EmailSender emailSender;

    public TicketService(UserRepository userRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public List<Ticket> findAllTickets() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .flatMap(user -> user.getTickets().stream())
                .collect(Collectors.toList());
    }

    public List<Ticket> getUsersTickets(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Entity not found"));
        return user.getTickets();
    }

    public Ticket addTicket(Ticket ticket, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Entity not found"));
        Ticket ticketWithDetails = getTicketWithDetails(ticket);
        addTicketToUser(user, ticketWithDetails);
        emailSender.sendEmail(user, ticket);
        userRepository.save(user);
        return ticket;
    }

    private Ticket getTicketWithDetails(Ticket ticket) {
        TicketType ticketType = ticket.getType();
        int ticketDuration = ticketType.getDuration();
        ticket.setTicketValidity(LocalDateTime.now().plusMinutes(ticketDuration));
        return ticket;
    }

    private void addTicketToUser(User user, Ticket ticket) {
        List<Ticket> usersTickets = user.getTickets();
        usersTickets.add(ticket);
    }

    public List<Ticket> getInactiveTickets() {
        List<Ticket> allTickets = findAllTickets();
        return allTickets.stream()
                .filter(ticket -> ticket.getTicketValidity().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public void deleteInactiveTickets() {
        List<User> allUsers = userRepository.findAll();
        allUsers.forEach(user -> user.getTickets()
                .removeIf(this::isTicketInactive));
    }

    private boolean isTicketInactive(Ticket ticket) {
        LocalDateTime ticketValidity = ticket.getTicketValidity();
        return ticketValidity.isBefore(LocalDateTime.now());
    }
}
