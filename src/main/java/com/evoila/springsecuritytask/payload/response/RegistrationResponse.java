package com.evoila.springsecuritytask.payload.response;


import lombok.*;


@Getter
@Setter
@Builder
public class RegistrationResponse {

    private String message;

    private String username;

    private String email;
}
