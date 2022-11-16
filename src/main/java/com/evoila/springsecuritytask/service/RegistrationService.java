package com.evoila.springsecuritytask.service;


import com.evoila.springsecuritytask.payload.request.RegistrationRequest;
import com.evoila.springsecuritytask.payload.response.RegistrationResponse;

public interface RegistrationService {

    RegistrationResponse register(RegistrationRequest registrationRequest);
}
