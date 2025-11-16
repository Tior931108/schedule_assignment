package com.example.common.exception;

import lombok.Getter;

@Getter
public class ExistEmailException extends CustomException{

    public ExistEmailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
