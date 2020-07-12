package springresttest.buyaticket.validation;

import org.springframework.beans.factory.annotation.Autowired;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private UserRepository userRepository;

    @Autowired
    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        List<User> userWithGivenEmail = userRepository.findByEmail(email);
        return userWithGivenEmail.isEmpty();
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }
}
