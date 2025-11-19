package com.example.common.exception;

import lombok.Getter;

@Getter
public class ExistAndNewPasswordException extends CustomException {

    public ExistAndNewPasswordException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
