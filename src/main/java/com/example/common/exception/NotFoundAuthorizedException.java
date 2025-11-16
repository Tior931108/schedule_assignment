package com.example.common.exception;

import lombok.Getter;

@Getter
public class NotFoundAuthorizedException extends CustomException {


    public NotFoundAuthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
