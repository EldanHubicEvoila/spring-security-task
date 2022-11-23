package com.evoila.springsecuritytask.payload.request;


import com.evoila.springsecuritytask.validators.RoleNameConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull(message = "username can't be null")
    @NotEmpty(message = "username can't be empty")
    private String username;

    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    private String password;

    @NotNull(message = "email can't be null")
    @NotEmpty(message = "email can't be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "role can't be null")
    @NotEmpty(message = "role can't be empty")
    @RoleNameConstraint
    private String role;
}
