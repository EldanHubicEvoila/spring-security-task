package com.evoila.springsecuritytask.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE })
@Constraint(validatedBy = RoleValidator.class)
public @interface RoleNameConstraint {

    String message() default "Invalid role name, must be: admin or user";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
