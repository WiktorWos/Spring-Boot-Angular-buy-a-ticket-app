package springresttest.buyaticket.validation;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
