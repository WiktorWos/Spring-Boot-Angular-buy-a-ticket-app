package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.exceptions.UsedEmailException;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.exceptions.WrongCredentialsException;
import springresttest.buyaticket.model.AuthenticationRequest;
import springresttest.buyaticket.model.AuthenticationResponse;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;
import springresttest.buyaticket.service.MyUserDetailsService;
import springresttest.buyaticket.util.JwtUtil;
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
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserRepository userRepository, AuthenticationManager authenticationManager,
                          MyUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws RuntimeException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch(BadCredentialsException e) {
            throw new WrongCredentialsException("Wrong email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
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
            throw new UsedEmailException("This email is already used.");
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