package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.authentication.JwtResponseDto;
import com.asmdev.api.pos.dto.authentication.LoginRequestDto;
import com.asmdev.api.pos.dto.authentication.MessageResponseDto;
import com.asmdev.api.pos.dto.authentication.SignupRequestDto;

public interface AuthService {


    JwtResponseDto authenticateUser(LoginRequestDto loginRequestDto);
    MessageResponseDto registerUser(SignupRequestDto signupRequestDto);


}
