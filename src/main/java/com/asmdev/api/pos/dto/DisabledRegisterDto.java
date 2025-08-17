package com.asmdev.api.pos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
