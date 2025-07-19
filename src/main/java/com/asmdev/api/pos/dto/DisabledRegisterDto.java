package com.asmdev.api.pos.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class DisabledRegisterDto implements Serializable {

    @NotNull(message = "El estado es obligatorio")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
