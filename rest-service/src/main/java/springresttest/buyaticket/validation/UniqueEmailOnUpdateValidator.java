package springresttest.buyaticket.validation;

import org.springframework.beans.factory.annotation.Autowired;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class UniqueEmailOnUpdateValidator implements ConstraintValidator<UniqueEmailOnUpdate, String> {
    private UserRepository userRepository;

    @Autowired
    public UniqueEmailOnUpdateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
//        List<User> usersWithGivenEmail = userRepository.findByEmail(email);
//        Optional<User> userWithGivenEmail = userRepository.findByEmail(email);
        return true;
    }

    @Override
    public void initialize(UniqueEmailOnUpdate constraintAnnotation) {

    }
}
