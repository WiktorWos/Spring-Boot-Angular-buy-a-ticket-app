package springresttest.buyaticket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import springresttest.buyaticket.model.Ticket;

import java.util.List;

@Document(collection = "user")
public class User {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<Ticket> tickets;

    public User(String firstName, String lastName, String email, List<Ticket> tickets) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tickets = tickets;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }
}
