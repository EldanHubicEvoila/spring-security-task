package com.evoila.springsecuritytask.exception.exceptionModel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class ErrorMessage {

    private int statusCode;
    private Date timeStamp;
    private String message;
    private String description;


    public ErrorMessage(int statusCode,
                        Date timeStamp,
                        String message,
                        String description) {
        this.statusCode = statusCode;
        this.timeStamp = timeStamp;
        this.message = message;
        this.description = description;
    }
}
