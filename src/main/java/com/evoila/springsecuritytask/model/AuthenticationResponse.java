package com.evoila.springsecuritytask.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class AuthenticationResponse {

    private String username;
    private String accessToken;
    private String email;
}
