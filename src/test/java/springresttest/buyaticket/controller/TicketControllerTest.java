package springresttest.buyaticket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springresttest.buyaticket.jackson.EntityToJson;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    private EntityToJson entityToJson;
    private DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    void setUp() {
        entityToJson = new EntityToJson();
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

    @Test
    void getAllTickets() throws Exception {
        List<User> users = generateUserList();

        given(userRepository.findAll()).willReturn(users);

        User firstUser = users.get(0);
        Ticket firstUsersTicket = firstUser.getTickets().get(0);
        String ticketValidity = firstUsersTicket.getTicketValidity().format(dateTimeFormatter);
        mockMvc.perform(get("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("Normal")))
                .andExpect(jsonPath("$[0].ticketValidity", is(ticketValidity)));

    }

    private List<User> generateUserList() {
        Ticket ticket = new Ticket("Normal", LocalDateTime.now().plusMinutes(50));
        List<Ticket> tickets = Arrays.asList(ticket);
        User user1 = new User("firstname1","lastName1","email1@gmail.com",tickets);
        User user2 = new User("firstname2","lastName2","email2@gmail.com",tickets);
        List<User> users = Arrays.asList(user1, user2);
        return users;
    }

    @Test
    void getUsersTickets() throws Exception {
        User user = generateUser();

        given(userRepository.findById("1")).willReturn(Optional.of(user));

        List<Ticket> userTickets = user.getTickets();
        String firstTicketValidityString = userTickets.get(0).getTicketValidity().format(dateTimeFormatter);
        String secondTicketValidityString = userTickets.get(1).getTicketValidity().format(dateTimeFormatter );

        mockMvc.perform(get("/api/tickets/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("Normal")))
                .andExpect(jsonPath("$[0].ticketValidity", is(firstTicketValidityString)))
                .andExpect(jsonPath("$[1].type", is("Reduced")))
                .andExpect(jsonPath("$[1].ticketValidity", is(secondTicketValidityString)));

    }

    private User generateUser() {
        Ticket ticket = new Ticket("Normal", LocalDateTime.now().plusMinutes(50));
        Ticket ticket1 = new Ticket("Reduced", LocalDateTime.now().plusHours(1));
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        tickets.add(ticket1);
        User user = new User("FirstName", "LastName", "email@gmail.com", tickets);
        return user;
    }

    @Test
    void addTicket() throws Exception {
        User user = generateUser();
        user.setUserId("1");
        Ticket newTicket = generateTicket();
        given(userRepository.findById("1")).willReturn(Optional.of(user));

        String jsonTicketString = entityToJson.convertToJson(newTicket);
        LocalDateTime nowPlus50min = LocalDateTime.now().plusMinutes(50);
        String formattedDate = nowPlus50min.format(dateTimeFormatter);

        mockMvc.perform(put("/api/tickets/{userId}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTicketString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type",is(newTicket.getType())))
                .andExpect(jsonPath("$.ticketValidity",is(formattedDate)));
    }

    private Ticket generateTicket() {
        return new Ticket("Normal");
    }

    @Test
    void deleteTicket() throws Exception{
        List<User> usersWithInactiveTickets = generateUserListWithInactiveTickets();

        given(userRepository.findAll()).willReturn(usersWithInactiveTickets);

        User user1 = usersWithInactiveTickets.get(0);
        User user2 = usersWithInactiveTickets.get(1);
        Ticket ticket1 = user1.getTickets().get(0);
        Ticket ticket2 = user2.getTickets().get(0);
        String ticket1Validity = ticket1.getTicketValidity().format(dateTimeFormatter);
        String ticket2Validity = ticket2.getTicketValidity().format(dateTimeFormatter);

        mockMvc.perform(delete("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type",is(ticket1.getType())))
                .andExpect(jsonPath("$[0].ticketValidity",is(ticket1Validity)))
                .andExpect(jsonPath("$[1].type",is(ticket2.getType())))
                .andExpect(jsonPath("$[1].ticketValidity",is(ticket2Validity)));
    }

    private List<User> generateUserListWithInactiveTickets() {
        List<Ticket> inactiveTickets = new ArrayList<>();
        List<User> usersWithInactiveTickets = new ArrayList<>();
        Ticket ticket = new Ticket("Normal", LocalDateTime.now().minusHours(1));
        inactiveTickets.add(ticket);
        User user1 = new User("1","1","1@email.com", inactiveTickets);
        User user2 = new User("2","2","2@email.com", inactiveTickets);
        usersWithInactiveTickets.add(user1);
        usersWithInactiveTickets.add(user2);
        return usersWithInactiveTickets;
    }
}