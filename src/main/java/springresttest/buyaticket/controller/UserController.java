package springresttest.buyaticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserRepository userRepository;
    //private PdfGenerator pdfGenerator;
    //private QrCodeGenerator qrCodeGenerator;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.pdfGenerator = pdfGenerator;
        //this.qrCodeGenerator = qrCodeGenerator;
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getOneUser(@PathVariable String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    private User getUserWithUpdatedProperties(User userToUpdate, User newUser) {
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setTickets(newUser.getTickets());
        return userToUpdate;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable String id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
        User userWithUpdatedProperties = getUserWithUpdatedProperties(userToUpdate,newUser);
        userRepository.save(userWithUpdatedProperties);
        return userWithUpdatedProperties;
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> userRepository.delete(user));
        return optionalUser.orElseThrow(() -> new RuntimeException("Entity not found"));
    }
}