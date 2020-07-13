package springresttest.buyaticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.security.MyUserPrincipal;
import springresttest.buyaticket.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        List<User> usersFoundByEmail = userRepository.findByEmail(email);
        if(usersFoundByEmail.isEmpty()) throw new UsernameNotFoundException(email);
        User user = usersFoundByEmail.get(0);
        return new MyUserPrincipal(user);
    }
}
