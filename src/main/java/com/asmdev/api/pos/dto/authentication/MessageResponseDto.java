package com.asmdev.api.pos.dto.authentication;

import java.io.Serializable;

public class MessageResponseDto implements Serializable {

    private String message;

    public MessageResponseDto(){}

    public MessageResponseDto(String message) {
        this.message = message;

    }

}
