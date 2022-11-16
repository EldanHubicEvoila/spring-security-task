package com.evoila.springsecuritytask.service;


import com.evoila.springsecuritytask.payload.request.AuthenticationRequest;
import com.evoila.springsecuritytask.payload.response.AuthenticationResponse;


public interface AuthenticationService {

     AuthenticationResponse login(AuthenticationRequest request);
}
