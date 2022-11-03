package com.evoila.springsecuritytask.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthRequest {

    @NotNull(message = "username can't be null")
    private String username;

    @NotNull(message = "password can't be null")
    private String password;
}
