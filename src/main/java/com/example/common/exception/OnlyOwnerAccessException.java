package com.example.common.exception;

import lombok.Getter;

@Getter
public class OnlyOwnerAccessException extends CustomException {

    public OnlyOwnerAccessException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
