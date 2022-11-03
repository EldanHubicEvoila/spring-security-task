package com.evoila.springsecuritytask.service;

import com.evoila.springsecuritytask.model.AuthenticationRequest;
import com.evoila.springsecuritytask.model.AuthenticationResponse;


public interface AuthenticationService {

     AuthenticationResponse login(AuthenticationRequest request);
}
