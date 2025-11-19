package com.example.common.exception;

import lombok.Getter;

@Getter
public class NeedToLoginException extends CustomException {

    public NeedToLoginException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
