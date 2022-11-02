package com.evoila.springsecuritytask.exception.exceptionModel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorDetails extends ErrorMessage{

    private List<String> errorList;


    public ErrorDetails(int statusCode,
                        Date timeStamp,
                        String message,
                        String description,
                        List<String> errorList) {
        super(statusCode, timeStamp, message, description);
        this.errorList = errorList;
    }
}
