package com.evoila.springsecuritytask.validators;

import com.evoila.springsecuritytask.model.ERole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<RoleNameConstraint, String> {


    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        String roleName = role.toUpperCase();

        return roleName.equals(ERole.USER.name())
                || roleName.equals(ERole.ADMIN.name());
    }
}
