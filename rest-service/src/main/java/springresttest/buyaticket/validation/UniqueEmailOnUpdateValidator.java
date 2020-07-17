package springresttest.buyaticket.validation;

import org.springframework.beans.factory.annotation.Autowired;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class UniqueEmailOnUpdateValidator implements ConstraintValidator<UniqueEmailOnUpdate, String> {

    @Autowired
    public UniqueEmailOnUpdateValidator() {
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }

    @Override
    public void initialize(UniqueEmailOnUpdate constraintAnnotation) {

    }
}
