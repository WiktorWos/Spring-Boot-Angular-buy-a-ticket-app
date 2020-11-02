package springresttest.buyaticket.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.service.UserService;

import java.util.List;

@Component
public class UserSecurity {
    private UserService userService;

    @Autowired
    public UserSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean isUsersIdSameAsURL(Authentication authentication, String userId) {
        MyUserPrincipal myUserPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        String email = myUserPrincipal.getEmail();
        User user = userService.findByEmail(email).get(0);
        return user.getUserId().equals(userId);
    }
}
