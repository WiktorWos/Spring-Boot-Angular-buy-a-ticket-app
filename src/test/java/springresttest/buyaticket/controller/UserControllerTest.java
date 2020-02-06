package springresttest.buyaticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springresttest.buyaticket.jackson.UserToJson;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserToJson userToJson;

    private List<User> generateUserList() {
        Ticket ticket = new Ticket("Normal", LocalDateTime.now());
        List<Ticket> tickets = Arrays.asList(ticket);
        User user1 = new User("firstname1","lastName1","email1",tickets);
        User user2 = new User("firstname2","lastName2","email2",tickets);
        List<User> users = Arrays.asList(user1, user2);
        return users;
    }
    @Test
    void getAllUsers() throws Exception {
        List<User> users = generateUserList();

        given(userRepository.findAll()).willReturn(users);

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

    private User generateUser() {
        List<Ticket> tickets = new ArrayList<>();
        User user = new User("firstName1","lastName1","email1",tickets);
        return user;
    }
    @Test
    void getOneUser() throws Exception {
        User user = generateUser();
        user.setUserId("1");

        given(userRepository.findById("1")).willReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(user.getUserId())));
    }

    @Test
    void addUser() throws Exception {
        UserToJson userToJson = new UserToJson();
        String jsonUserString;

        User user = generateUser();
        jsonUserString = userToJson.convertToJson(user);

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

    private User generateUpdatedUser() {
        List<Ticket> tickets = new ArrayList<>();
        User user = new User("updatedName","updatedLastName","updatedEmail",tickets);
        return user;
    }
    @Test
    void updateUser() throws Exception {
        UserToJson userToJson = new UserToJson();
        String jsonUpdatedUserString;

        User user = generateUser();
        user.setUserId("1");
        User updatedUser = generateUpdatedUser();
        updatedUser.setUserId("1");

        given(userRepository.findById("1")).willReturn(Optional.of(user));

        jsonUpdatedUserString = userToJson.convertToJson(updatedUser);

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
    void deleteUser() throws Exception {
        User user = generateUser();
        user.setUserId("1");

        given(userRepository.findById("1")).willReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(user.getUserId())));
    }
}