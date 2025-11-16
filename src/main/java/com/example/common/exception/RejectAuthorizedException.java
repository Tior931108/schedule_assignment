package com.example.common.exception;

import lombok.Getter;

@Getter
public class RejectAuthorizedException extends CustomException {

    public RejectAuthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
