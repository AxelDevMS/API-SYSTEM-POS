package com.asmdev.api.pos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateInputDto implements Serializable {

    private String input;
    private String invalidMessage;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInvalidMessage() {
        return invalidMessage;
    }

    public void setInvalidMessage(String invalidMessage) {
        this.invalidMessage = invalidMessage;
    }
}
