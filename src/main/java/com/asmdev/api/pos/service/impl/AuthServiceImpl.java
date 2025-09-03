package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.authentication.JwtResponseDto;
import com.asmdev.api.pos.dto.authentication.LoginRequestDto;
import com.asmdev.api.pos.dto.authentication.MessageResponseDto;
import com.asmdev.api.pos.dto.authentication.SignupRequestDto;
import com.asmdev.api.pos.persistence.repository.RoleRepository;
import com.asmdev.api.pos.persistence.repository.UserRepository;
import com.asmdev.api.pos.security.CustomUserDetails;
import com.asmdev.api.pos.security.jwt.JwtService;
import com.asmdev.api.pos.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Override
    public JwtResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toList());

        List<String> permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE_"))
                .collect(Collectors.toList());

        return new JwtResponseDto(
                jwt,
                userDetails.getUser().getId(),
                userDetails.getUser().getEmail(),
                roles,
                permissions
        );
    }

    @Override
    public MessageResponseDto registerUser(SignupRequestDto signupRequestDto) {
        return null;
    }

}
