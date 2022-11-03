package com.evoila.springsecuritytask.exception.response;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
public class ErrorResponse {

    private int statusCode;
    private Date timeStamp;
    private String message;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> validationErrors;
}
