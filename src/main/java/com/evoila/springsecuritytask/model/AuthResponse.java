package com.evoila.springsecuritytask.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {

    private String username;
    private String accessToken;
    private String email;


    public AuthResponse(String username,
                        String email,
                        String accessToken) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
    }
}
