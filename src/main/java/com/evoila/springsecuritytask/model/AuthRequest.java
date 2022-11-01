package com.evoila.springsecuritytask.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
