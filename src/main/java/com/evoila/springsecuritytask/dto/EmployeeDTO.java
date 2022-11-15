package com.evoila.springsecuritytask.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
public class EmployeeDTO {

    private Long id;

    @NotNull(message = "firstName can't be null")
    private String firstName;

    @NotNull(message = "lastName can't be null")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotNull(message = "email can't be null")
    private String email;
}
