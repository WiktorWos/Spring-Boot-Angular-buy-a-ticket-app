package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.model.AuthenticationRequest;
import springresttest.buyaticket.model.AuthenticationResponse;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.security.MyUserPrincipal;
import springresttest.buyaticket.service.UserService;
import springresttest.buyaticket.validation.OnCreate;
import springresttest.buyaticket.validation.OnUpdate;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate/signIn")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws RuntimeException {
        userService.authenticate(authenticationRequest);
        MyUserPrincipal userDetails = (MyUserPrincipal) userService.getUserDetailsFromAuthRequest(authenticationRequest);
        String jwt = userService.generateJwt(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, userDetails.getFirstName(), userDetails.getLastName(),
                                                             userDetails.getEmail(), userDetails.getTickets()));
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getOneUser(@PathVariable String id) {
        return userService.findById(id).orElseThrow(() -> new UserNotFoundException("Entity not found"));
    }

    @Validated(OnCreate.class)
    @PostMapping("/authenticate/signUp")
    public User addUser(@Valid @RequestBody User user) {
        userService.save(user);
        return user;
    }

    @Validated(OnUpdate.class)
    @PutMapping("/users/{id}")
    public User updateUser(@Valid @RequestBody User updatedUser, @PathVariable String id) {
        userService.updateUser(updatedUser, id);
        return updatedUser;
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = userService.findById(id);
        optionalUser.ifPresent(user -> userService.delete(user));
        return optionalUser.orElseThrow(() -> new UserNotFoundException("Entity not found"));
    }
}