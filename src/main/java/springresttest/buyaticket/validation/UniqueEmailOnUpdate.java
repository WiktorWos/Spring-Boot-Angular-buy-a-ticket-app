package springresttest.buyaticket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailOnUpdateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface UniqueEmailOnUpdate {
    public String message() default "There is already user with this email!";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default{};
}
