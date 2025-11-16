package com.example.common.exception;

import lombok.Getter;

@Getter
public class NotMatchPasswordException extends CustomException {


    public NotMatchPasswordException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
