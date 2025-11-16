package com.example.common.exception;

public class NotFoundAuthorizedException extends CustomException {


    public NotFoundAuthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
