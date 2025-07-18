package com.asmdev.api.pos.exception;

import com.asmdev.api.pos.dto.ApiResponseFailedDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        ApiResponseFailedDto response = new ApiResponseFailedDto();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Internal Server Error");
        response.setDebugMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseFailedDto> notFoundException(Exception e){
        ApiResponseFailedDto response = new ApiResponseFailedDto();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage("Not Found Error");
        response.setDebugMessage(e.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseFailedDto> badRequestException(Exception e, BadRequestException badRequestException){
        ApiResponseFailedDto response = new ApiResponseFailedDto();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Bad Request Error");
        response.setDebugMessage(e.getMessage());
        response.setSubErrors(badRequestException.getSubErrors());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



}
