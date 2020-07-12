package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;
import springresttest.buyaticket.validation.OnCreate;
import springresttest.buyaticket.validation.OnUpdate;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getOneUser(@PathVariable String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Entity not found"));
    }

    @Validated(OnCreate.class)
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @Validated(OnUpdate.class)
    @PutMapping("/users/{id}")
    public User updateUser(@Valid @RequestBody User newUser, @PathVariable String id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Entity not found"));
        String newUsersEmail = newUser.getEmail();
        List<User> usersFoundByGivenEmail = userRepository.findByEmail(newUsersEmail);
        if(!usersFoundByGivenEmail.isEmpty()) {
            User user = usersFoundByGivenEmail.get(0);
            checkIfNewEmailIsAlreadyUsed(user, id);
        }
        User userWithUpdatedProperties = getUserWithUpdatedProperties(userToUpdate, newUser);
        userRepository.save(userWithUpdatedProperties);
        return userWithUpdatedProperties;
    }

    private void checkIfNewEmailIsAlreadyUsed(User userFoundByGivenEmail, String pathVariableId) {
        String foundUsersId = userFoundByGivenEmail.getUserId();
        if (!foundUsersId.equals(pathVariableId)) {
            throw new RuntimeException("This email is already used.");
        }
    }

    private User getUserWithUpdatedProperties(User userToUpdate, User newUser) {
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setTickets(newUser.getTickets());
        return userToUpdate;
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> userRepository.delete(user));
        return optionalUser.orElseThrow(() -> new UserNotFoundException("Entity not found"));
    }
}