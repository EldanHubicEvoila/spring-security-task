package com.evoila.springsecuritytask.exception.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class ErrorResponse {

    private int statusCode;
    private Date timeStamp;
    private String message;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> validationErrors;


    public ErrorResponse(int statusCode,
                         Date timeStamp,
                         String message,
                         String description) {
        this.statusCode = statusCode;
        this.timeStamp = timeStamp;
        this.message = message;
        this.description = description;
    }

    public ErrorResponse(int statusCode,
                         Date timeStamp,
                         String message,
                         String description,
                         List<String> validationErrors) {
        this(statusCode, timeStamp, message, description);
        this.validationErrors = validationErrors;
    }
}
