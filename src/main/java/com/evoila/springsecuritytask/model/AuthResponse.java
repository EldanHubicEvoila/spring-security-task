package com.evoila.springsecuritytask.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {

    private @NonNull String username;
    private @NonNull String accessToken;
    private @NonNull String email;


    public AuthResponse(@NonNull String username,@NonNull String email,@NonNull String accessToken) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
    }
}
