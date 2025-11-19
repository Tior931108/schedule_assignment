package com.example.common.exception;

import lombok.Getter;

@Getter
public class NotMatchEmailException extends CustomException {

    public NotMatchEmailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
