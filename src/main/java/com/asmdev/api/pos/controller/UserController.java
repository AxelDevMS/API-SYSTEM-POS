package com.asmdev.api.pos.controller;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetListUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) String municipality,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String hireDate
    ) throws NotFoundException {
        ApiResponseDto response = this.userService.executeGetListUsers(page, size, userId, status, roleId, neighborhood, municipality, state, hireDate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-select")
    public ResponseEntity<ApiResponseDto> executeGetListBySelect() throws NotFoundException {
        ApiResponseDto response = this.userService.executeGetLisUsersBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponseDto> executeGetUser(@PathVariable String userId) throws NotFoundException {
        ApiResponseDto response = this.userService.executeGetUser(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateUser(
            @Valid @RequestBody UserDto userDto,
            BindingResult bindingResult
            ) throws BadRequestException, NotFoundException {

        ApiResponseDto response = this.userService.executeCreateUser(userDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponseDto> executeUpdateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserDto userDto,
            BindingResult bindingResult
    ) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.userService.executeUpdateUser(userId, userDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("disabled/{userId}")
    public ResponseEntity<ApiResponseDto> executeDisabledUser(
            @PathVariable String userId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.userService.executeDisabledUser(userId, disabledRegisterDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
