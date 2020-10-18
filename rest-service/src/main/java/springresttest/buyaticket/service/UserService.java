package springresttest.buyaticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springresttest.buyaticket.exceptions.UsedEmailException;
import springresttest.buyaticket.exceptions.UserNotFoundException;
import springresttest.buyaticket.exceptions.WrongCredentialsException;
import springresttest.buyaticket.model.AuthenticationRequest;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;
import springresttest.buyaticket.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService userDetailsService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       MyUserDetailsService userDetailsService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        User userWithEncodedPassword = getUserWithEncodedPassword(user);
        userRepository.save(userWithEncodedPassword);
    }

    private User getUserWithEncodedPassword(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateJwt(UserDetails userDetails) {
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }

    public UserDetails getUserDetailsFromAuthRequest(AuthenticationRequest authenticationRequest) {
        return userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    }

    public void authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch(BadCredentialsException e) {
            throw new WrongCredentialsException("Wrong email or password");
        }
    }

    public void updateUser(User updatedUser, String pathVariableId) {
        User userToUpdate = findById(pathVariableId).orElseThrow(() -> new UserNotFoundException("Entity not found"));
        String updatedUsersId = userToUpdate.getUserId();
        String newUsersEmail = updatedUser.getEmail();
        List<User> usersFoundByGivenEmail = findByEmail(newUsersEmail);

        if(!usersFoundByGivenEmail.isEmpty()) {
            User user = usersFoundByGivenEmail.get(0);
            checkIfNewEmailIsAlreadyUsed(user, pathVariableId);
        }

        updatedUser.setUserId(updatedUsersId);
        save(updatedUser);
    }

    private void checkIfNewEmailIsAlreadyUsed(User userFoundByGivenEmail, String pathVariableId) {
        String foundUsersId = userFoundByGivenEmail.getUserId();
        if (!foundUsersId.equals(pathVariableId)) {
            throw new UsedEmailException("This email is already used.");
        }
    }
}

