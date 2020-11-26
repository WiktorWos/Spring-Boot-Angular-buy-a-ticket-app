package springresttest.buyaticket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.TicketType;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.security.UserSecurity;
import springresttest.buyaticket.service.MyUserDetailsService;
import springresttest.buyaticket.service.TicketService;
import springresttest.buyaticket.service.UserService;
import springresttest.buyaticket.util.JwtUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@WithMockUser
class TicketControllerTest {

    @TestConfiguration
    public static class TestConfig {
        @Bean
        UserSecurity userSecurity() {
            return new UserSecurity(userService);
        }

        @MockBean
        UserService userService;
    }

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TicketService ticketService;

    @MockBean
    MyUserDetailsService userDetailsService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    UserSecurity userSecurity;

    private DateTimeFormatter dateTimeFormatter;

    @BeforeEach
    void setUp() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

    @Test
    void getAllTickets() throws Exception {
        List<Ticket> tickets = generateTicketList();
        Ticket firstTicket = tickets.get(0);
        String ticketValidity = firstTicket.getTicketValidity().format(dateTimeFormatter);

        given(ticketService.findAllTickets()).willReturn(tickets);

        mockMvc.perform(get("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type.price", is(1.50)))
                .andExpect(jsonPath("$[0].type.duration", is(20)))
                .andExpect(jsonPath("$[0].ticketValidity", is(ticketValidity)));

    }

    private List<User> generateUserList() {
        Ticket ticket = new Ticket(TicketType.REDUCED_20, LocalDateTime.now().plusMinutes(50));
        List<Ticket> tickets = Arrays.asList(ticket);
        User user1 = new User("firstname1","lastName1","email1@gmail.com", "pass" ,tickets);
        User user2 = new User("firstname2","lastName2","email2@gmail.com", "pass",tickets);
        List<User> users = Arrays.asList(user1, user2);
        return users;
    }

    private List<Ticket> generateTicketList() {
        Ticket ticket = new Ticket(TicketType.REDUCED_20, LocalDateTime.now().plusMinutes(50));
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        return tickets;
    }

    @Test
    void getUsersTickets() throws Exception {
        List<Ticket> tickets = generateTicketList();
        LocalDateTime ticketValidity = tickets.get(0).getTicketValidity();
        String validityString = ticketValidity.format(dateTimeFormatter);

        given(ticketService.getUsersTickets(ArgumentMatchers.any())).willReturn(tickets);
        given(userSecurity.isUsersIdSameAsURL(ArgumentMatchers.any(), ArgumentMatchers.anyString())).willReturn(true);

        mockMvc.perform(get("/api/tickets/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type.price", is(1.50)))
                .andExpect(jsonPath("$[0].type.duration", is(20)))
                .andExpect(jsonPath("$[0].ticketValidity", is(validityString)));
    }

    private User generateUser() {
        Ticket ticket = new Ticket(TicketType.NORMAL_20, LocalDateTime.now().plusMinutes(50));
        Ticket ticket1 = new Ticket(TicketType.REDUCED_20, LocalDateTime.now().plusHours(1));
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        tickets.add(ticket1);
        User user = new User("FirstName", "LastName", "email@gmail.com","pass", tickets);
        return user;
    }

    @Test
    void addTicket() throws Exception {
        User user = generateUser();
        user.setUserId("1");
        Ticket newTicket = generateTicket();
        given(ticketService.addTicket(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(newTicket);
        given(userSecurity.isUsersIdSameAsURL(ArgumentMatchers.any(), ArgumentMatchers.anyString())).willReturn(true);

        String jsonTicketString = "{\"type\":\"NORMAL_20\"}";
        LocalDateTime nowPlus20min = LocalDateTime.now().plusMinutes(20);
        String formattedDate = nowPlus20min.format(dateTimeFormatter);
        mockMvc.perform(put("/api/tickets/{userId}", user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTicketString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type.price",is(newTicket.getType().getPrice())))
                .andExpect(jsonPath("$.type.duration",is(newTicket.getType().getDuration())))
                .andExpect(jsonPath("$.ticketValidity",is(formattedDate)));
    }

    private Ticket generateTicket() {
        return new Ticket(TicketType.NORMAL_20, LocalDateTime.now().plusMinutes(20));
    }

    @Test
    void deleteTicket() throws Exception{
        List<Ticket> inactiveTickets = generateInactiveTickets();
        Ticket ticket = inactiveTickets.get(0);
        String ticketValidity = ticket.getTicketValidity().format(dateTimeFormatter);

        given(ticketService.getInactiveTickets()).willReturn(inactiveTickets);

        mockMvc.perform(delete("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type.price",is(ticket.getType().getPrice())))
                .andExpect(jsonPath("$[0].type.duration",is(ticket.getType().getDuration())))
                .andExpect(jsonPath("$[0].ticketValidity",is(ticketValidity)));
    }

    private List<Ticket> generateInactiveTickets() {
        List<Ticket> inactiveTickets = new ArrayList<>();
        Ticket ticket = new Ticket(TicketType.NORMAL_20, LocalDateTime.now().minusHours(1));
        inactiveTickets.add(ticket);
        return inactiveTickets;
    }
}