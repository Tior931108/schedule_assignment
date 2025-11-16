package com.example.common.exception;

public class RejectAuthorizedException extends CustomException {

    public RejectAuthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
