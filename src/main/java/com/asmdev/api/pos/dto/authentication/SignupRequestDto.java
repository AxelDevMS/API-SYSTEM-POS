package com.asmdev.api.pos.dto.authentication;

import java.util.List;

public class SignupRequestDto {
    private String email;
    private String password;
    private List<String> roles;
}
