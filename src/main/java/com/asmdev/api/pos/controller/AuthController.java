package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.authentication.JwtResponseDto;
import com.asmdev.api.pos.dto.authentication.LoginRequestDto;
import com.asmdev.api.pos.dto.authentication.MessageResponseDto;
import com.asmdev.api.pos.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pos/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        try {
            JwtResponseDto response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDto("Error: Credenciales inválidas!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDto("Error: " + e.getMessage()));
        }
    }

    /*@PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            MessageResponse response = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }*/

    @PostMapping("/refresh")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // Implementar lógica de refresh token si es necesario
        return ResponseEntity.ok(new MessageResponseDto("Token refresh no implementado"));
    }


}
