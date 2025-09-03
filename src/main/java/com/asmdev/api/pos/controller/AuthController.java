package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.authentication.JwtResponseDto;
import com.asmdev.api.pos.dto.authentication.LoginRequestDto;
import com.asmdev.api.pos.dto.authentication.MessageResponseDto;
import com.asmdev.api.pos.security.jwt.JwtAuthenticationEntryPoint;
import com.asmdev.api.pos.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid  @RequestBody LoginRequestDto loginRequest) {
        logger.info("ENTRA ALA FUNCION");
        try {
            logger.info("ENTRA AL TRY");
            JwtResponseDto response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.info("ENTRA AL catrch");
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDto("Error: Credenciales inválidas!"));
        } catch (Exception e) {
            logger.info("ENTRA AL catrch");

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
