package springresttest.buyaticket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import springresttest.buyaticket.exceptions.UsedEmailException;
import springresttest.buyaticket.exceptions.WrongCredentialsException;
import springresttest.buyaticket.jackson.EntityToJson;
import springresttest.buyaticket.model.AuthenticationRequest;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.TicketType;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.service.MyUserDetailsService;
import springresttest.buyaticket.service.UserService;
import springresttest.buyaticket.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    MyUserDetailsService userDetailsService;

    @MockBean
    JwtUtil jwtUtil;

    private EntityToJson entityToJson;

    @BeforeEach
    void setUp() {
        entityToJson = new EntityToJson();
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = generateUserList();

        given(userService.findAll()).willReturn(users);


        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(users.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(users.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())))
                .andExpect(jsonPath("$[1].firstName", is(users.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(users.get(1).getLastName())))
                .andExpect(jsonPath("$[1].email", is(users.get(1).getEmail())));

    }

    private List<User> generateUserList() {
        Ticket ticket = new Ticket(TicketType.NORMAL_20, LocalDateTime.now());
        List<Ticket> tickets = Arrays.asList(ticket);
        User user1 = new User("firstname1","lastName1","email1@gmail.com", "pass",tickets);
        User user2 = new User("firstname2","lastName2","email2@gmail.com", "pass",tickets);
        List<User> users = Arrays.asList(user1, user2);
        return users;
    }

    @Test
    void getOneUser() throws Exception {
        User user = generateUser();
        user.setUserId("1");

        given(userService.findById("1")).willReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(user.getUserId())));
    }

    private User generateUser() {
        List<Ticket> tickets = new ArrayList<>();
        User user = new User("firstName1","lastName1","email1@gmail.com", "pass",tickets);
        return user;
    }

    @Test
    void getOneUser_EntityNotFound() throws Exception {
        given(userService.findById("1")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message[0]", is("Entity not found")));
    }

    @Test
    void addUser() throws Exception {
        String jsonUserString;

        User user = generateUser();
        jsonUserString = entityToJson.convertToJson(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(user.getUserId())))
                .andExpect(jsonPath("$.firstName",is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(user.getLastName())))
                .andExpect(jsonPath("$.email",is(user.getEmail())));
    }

    @Test
    void addUser_UserWithoutLastName() throws Exception{
        User userWithoutLastName = generateUserWithoutLastName();
        String jsonUserString = entityToJson.convertToJson(userWithoutLastName);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message[0]", is("Please provide a last name")));
    }

    private User generateUserWithoutLastName() {
        List<Ticket> tickets = new ArrayList<>();
        User user = new User("firstName","","email@gmail.com", "pass",tickets);
        return user;
    }

    @Test
    void addUser_UserWithInvalidEmail() throws Exception{
        User userWithoutLastName = generateUserWithInvalidEmail();
        String jsonUserString = entityToJson.convertToJson(userWithoutLastName);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message[0]", is("Please provide a valid email address")));
    }

    private User generateUserWithInvalidEmail() {
        List<Ticket> tickets = new ArrayList<>();
        User user = new User("firstName","lastName","invalid", "pass",tickets);
        return user;
    }

    @Test
    void addUser_UserWithExistingEmail() throws Exception{
        User user = generateUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        String jsonUserString = entityToJson.convertToJson(user);

        given(userService.findByEmail(any())).willReturn(users);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message[0]", is("There is already user with this email!")));
    }

    @Test
    void updateUser() throws Exception {
        String jsonUpdatedUserString;
        User user = generateUser();
        user.setUserId("1");
        User updatedUser = generateUpdatedUser();
        updatedUser.setUserId("1");
        List<User> usersFoundByUpdatedEmail = new ArrayList<>();
        usersFoundByUpdatedEmail.add(user);

        given(userService.findByEmail(ArgumentMatchers.any())).willReturn(usersFoundByUpdatedEmail);
        given(userService.findById("1")).willReturn(Optional.of(user));

        jsonUpdatedUserString = entityToJson.convertToJson(updatedUser);

        mockMvc.perform(put("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdatedUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is(updatedUser.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(updatedUser.getLastName())))
                .andExpect(jsonPath("$.email",is(updatedUser.getEmail())));

    }

    @Test
    void updateUser_UserWithExistingEmail() throws Exception {
        String jsonUpdatedUserString;
        User updatedUser = generateUpdatedUser();
        jsonUpdatedUserString = entityToJson.convertToJson(updatedUser);

        doThrow(new UsedEmailException("This email is already used.")).when(userService).updateUser(any(), any());

        mockMvc.perform(put("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdatedUserString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message[0]", is("This email is already used.")));

    }

    private User generateUpdatedUser() {
        List<Ticket> tickets = new ArrayList<>();
        User user = new User("updatedName","updatedLastName","email1@gmail.com", "pass",tickets);
        return user;
    }


    @Test
    void deleteUser() throws Exception {
        User user = generateUser();
        user.setUserId("1");

        given(userService.findById("1")).willReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(user.getUserId())));
    }

    @Test
    void createAuthenticationToken() throws Exception {
        User user = generateUser();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getEmail(), user.getPassword());
        String jsonAuthenticationString = entityToJson.convertToJson(authenticationRequest);
        String jwt = "jwtAuthenticationTest";

        given(userService.generateJwt(ArgumentMatchers.any())).willReturn(jwt);

        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAuthenticationString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("jwt", is(jwt)));
    }

    @Test
    void createAuthenticationToken_withBadCredentials() throws Exception {
        User user = generateUser();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getEmail(), user.getPassword());
        String jsonAuthenticationString = entityToJson.convertToJson(authenticationRequest);

        given(userService.generateJwt(ArgumentMatchers.any()))
                .willThrow(new WrongCredentialsException("Wrong email or password"));

        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAuthenticationString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message[0]", is("Wrong email or password")));
    }
}