package com.evoila.springsecuritytask.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "firstName can't be null")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "lastName can't be null")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotNull(message = "email can't be null")
    @Column(name = "email")
    private String email;
}
